package ru.practicum.ewm_main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.EventMapper;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
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
        Request request;
        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new DataAlreadyExists(String.format("Запрос с requesterId=%d и eventId=%d уже существует", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new DataAlreadyExists(String.format("Инициатор события с id=%d не может добавить запрос на участие в своём событии", userId));
        }
        if (!event.getState().equals(ru.practicum.ewm_main_service.event.model.Status.PUBLISHED)) {
            throw new DataAlreadyExists(String.format("Событие с id=%d не опубликовано", eventId));
        }
        if (event.getParticipantLimit() == (event.getConfirmedRequests())) {
            throw new DataAlreadyExists(String.format("Событие с id=%d имеет максимальное количество заявок", eventId));
        }
        if (!event.isRequestModeration()) {
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
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();
        List<Long> requestIds = dto.getRequestIds();
        List<Request> requests = repository.findAllByIdIn(requestIds);
        String status = dto.getStatus();

        if (status.equals(RequestStatus.REJECTED.toString())) {
            if (status.equals(RequestStatus.REJECTED.toString())) {
                boolean isConfirmedRequest = requests.stream()
                        .anyMatch(r -> r.getStatus().equals(RequestStatus.CONFIRMED));
                if (isConfirmedRequest) {
                    throw new DataAlreadyExists("Невозможно отклонить подтвержденные запросы");
                }
                rejectedRequests = requests.stream()
                        .peek(r -> r.setStatus(RequestStatus.REJECTED))
                        .map(RequestMapper::toDto)
                        .collect(Collectors.toList());
                return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
            }
        }

        if (status.equals(RequestStatus.PENDING.name())) {
            Event event = eventService.findEventById(eventId).orElseThrow(() -> new DataNotFoundException("Событие не найдено или недоступно"));
            if ((!event.isRequestModeration()) || (event.getParticipantLimit() == 0)) {
                for (Request request : requests) {
                    request.setStatus(RequestStatus.CONFIRMED);
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
                return new EventRequestStatusUpdateResult(
                        requests.stream().map(RequestMapper::toDto).collect(Collectors.toList()),
                        rejectedRequests);
            } else if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                throw new DataAlreadyExists("Нельзя подтвердить заявку, лимит по заявкам на данное событие уже достигнут");
            } else if (event.getConfirmedRequests() <= event.getParticipantLimit()) {
                do {
                    for (Request request : requests) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    }
                } while (event.getConfirmedRequests() < event.getParticipantLimit());
                return new EventRequestStatusUpdateResult(
                        requests.stream().map(RequestMapper::toDto).collect(Collectors.toList()),
                        rejectedRequests);
            }
        } else {
            throw new DataAlreadyExists("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
