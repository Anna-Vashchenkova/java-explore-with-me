package ru.practicum.ewm_main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.ConflictException;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm_main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.dto.RequestMapper;
import ru.practicum.ewm_main_service.request.model.Request;
import ru.practicum.ewm_main_service.request.model.RequestStatus;
import ru.practicum.ewm_main_service.request.repository.RequestRepository;
import ru.practicum.ewm_main_service.user.User;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserService userService;
    private final EventService eventService;
    @Override
    public ParticipationRequestDto addNewRequest(Long userId, Long eventId) {
        Event event = eventService.findEventById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID = %s не найдено", eventId)));
        User user = userService.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId)));
        Request request = new Request();
        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException(String.format("Запрос с requesterId=%d и eventId=%d уже существует", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("Инициатор события с id=%d не может добавить запрос на участие в своём событии", userId));
        }
        if (!event.getState().equals(ru.practicum.ewm_main_service.event.model.Status.PUBLISHED)) {
            throw new ConflictException(String.format("Событие с id=%d не опубликовано", eventId));
        }
        if ((event.getParticipantLimit() > 0) && (event.getParticipantLimit() == (event.getConfirmedRequests()))) {
            throw new ConflictException(String.format("Событие с id=%d имеет максимальное количество заявок", eventId));
        }
        if ((event.getParticipantLimit() == 0) || (!event.isRequestModeration())) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.saveAfterRequest(event);
            request = repository.save(new Request(null, event, user, LocalDateTime.now(), RequestStatus.CONFIRMED));
        } else {
            request = repository.save(new Request(null, event, user, LocalDateTime.now(), RequestStatus.PENDING));
        }
        return RequestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        if (userService.findById(userId).isEmpty()) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId));
        }
        List<Request> result = repository.findAllByRequesterId(userId);
        return result.isEmpty() ? new ArrayList<>() : result.stream().map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto requestCancellationByRequester(Long userId, Long requestId) {
        if (userService.findById(userId).isEmpty()) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId));
        }
        Request request = repository.findByRequesterIdAndId(userId, requestId);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsForOwnEvent(Long userId, Long eventId) {
        eventService.getEventById(userId, eventId);
        List<Request> requests = repository.findAllByEventId(eventId);
        return requests.isEmpty() ? new ArrayList<>() : requests.stream().map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest dto) {
        EventFullDto event = eventService.getEventById(userId, eventId);
        if(dto.getStatus().equals("REJECTED")) {
            return rejectRequests(dto.getRequestIds());
        } else {
            return confirmRequests(event, dto.getRequestIds());
        }
    }

    private EventRequestStatusUpdateResult confirmRequests(EventFullDto event, List<Long> requestIds) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        final long[] temp = new  long[]{event.getConfirmedRequests()};
        final long[] limit = new  long[]{event.getParticipantLimit()};
        requestIds.stream().map(id -> repository.findById(id).orElseGet(null))
                .filter(it -> it != null)
                .forEach(it -> {
                    if (it.getStatus().equals(RequestStatus.PENDING)) {
                        if (canConfirm(temp[0], limit[0])) {
                            it.setStatus(RequestStatus.CONFIRMED);
                            repository.save(it);
                            temp[0]++;
                        } else {
                            throw new ConflictException("Лимит превышен");
                        }
                    }
                    if (it.getStatus().equals(RequestStatus.CONFIRMED)) {
                        confirmedRequests.add(RequestMapper.toDto(it));
                    } else {
                        rejectedRequests.add(RequestMapper.toDto(it));
                    }
                });
        eventService.updateConfirmationCount(event.getId(), temp[0]);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private boolean canConfirm(long temp, long limit) {
        return (limit == 0L) || (temp < limit);
    }

    private EventRequestStatusUpdateResult rejectRequests(List<Long> requestIds) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        requestIds.stream()
                .map(id -> repository.findById(id).orElseGet(null))
                .filter(it -> it != null)
                .forEach(it -> {
                            if (it.getStatus().equals(RequestStatus.PENDING)) {
                                it.setStatus(RequestStatus.REJECTED);
                                repository.save(it);
                            } else {
                                throw new ConflictException("Нельзя отклонить принятую заявку");
                            }
                            if (it.getStatus().equals(RequestStatus.CONFIRMED)) {
                                confirmedRequests.add(RequestMapper.toDto(it));
                            } else {
                                rejectedRequests.add(RequestMapper.toDto(it));
                            }
                        });
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
