package ru.practicum.ewm_main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.category.service.CategoryService;
import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.model.Location;
import ru.practicum.ewm_main_service.event.model.Status;
import ru.practicum.ewm_main_service.event.repository.EventRepository;
import ru.practicum.ewm_main_service.event.specification.EventSpecification;
import ru.practicum.ewm_main_service.exception.ConflictException;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventMapper eventMapper;

    @Override
    public EventFullDto addNewEvent(long userId, NewEventDto dto) {
        Category category = categoryService.get(dto.getCategory());
        LocalDateTime eventDate = LocalDateTime.parse(dto.getEventDate(), formatter);
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException(String.format("Поле eventDate %s должно содержать дату, которая еще не наступила.", eventDate));
        }
        Event event = repository.save(new Event(null,
                dto.getTitle(),
                dto.getAnnotation(),
                dto.getDescription(),
                category,
                Status.PENDING,
                Location.builder().lat(dto.getLocation().getLat()).lon(dto.getLocation().getLon()).build(),
                eventDate,
                LocalDateTime.now(),
                null,
                userService.get(userId),
                dto.isPaid(),
                dto.getParticipantLimit(),
                dto.isRequestModeration(),
                0,
                0
                ));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        if (userService.findById(userId).isEmpty()) {
            throw new DataNotFoundException(String.format("Пользователь с ID = %s не найден", userId));
        }
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Page<Event> allByUserId = repository.findAllByUserId(userId, PageRequest.of(from, size, sortById));
        return allByUserId.isEmpty() ? new ArrayList<>() : allByUserId.getContent().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(long userId, long eventId) {
        Event event = repository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID %s, созданное пользователем с ID %s, не найдено", eventId, userId)));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto) {
        Event resultEvent = repository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Событие с ID %s, созданное пользователем с ID %s, не найдено\"", eventId, userId)));
        LocalDateTime eventDate;
        if (dto.getEventDate() != null) {
            eventDate = LocalDateTime.parse(dto.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException(String.format("Поле eventDate %s должно содержать дату, которая еще не наступила.", eventDate));
            }
        }
        if (dto.getTitle() != null) {
            resultEvent.setTitle(dto.getTitle());
        }
        if (dto.getAnnotation() != null) {
            resultEvent.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            resultEvent.setDescription(dto.getDescription());
        }
        if (dto.getLocation() != null) {
            resultEvent.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        }
        if (dto.getPaid() != null) {
            resultEvent.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() > 0) {
            resultEvent.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            resultEvent.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getCategory() != null) {
            resultEvent.setCategory(categoryService.getOptionalCategoryById(dto.getCategory())
                    .orElseThrow(() -> new DataNotFoundException(String.format("Категория с ID %s не найдена",
                            dto.getCategory()))));
        }
        if (resultEvent.getState().equals(Status.PUBLISHED)) {
            throw new ConflictException("Событие не должно быть опубликовано");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction().toString().equals(UpdateEventUserRequest.StateAction.CANCEL_REVIEW.toString())) {
                resultEvent.setState(Status.CANCELED);
            } else if (UpdateEventUserRequest.StateAction.SEND_TO_REVIEW.toString().equals(dto.getStateAction().toString())) {
                resultEvent.setState(Status.PENDING);
            }
        }
        repository.save(resultEvent);
        log.info("Обновление пользователем события : {}", resultEvent.getTitle());
        return eventMapper.toEventFullDto(resultEvent);
    }

    @Override
    public List<EventFullDto> searchEvent(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        if ((users == null) || (users.isEmpty()) || (users.get(0) < 1)) {
            users = null;
        }
        if ((categories == null) || (categories.isEmpty()) || (categories.get(0) < 1)) {
            categories = null;
        }

        List<Status> statusEnum = null;
        if (states != null) {
            statusEnum = states.stream().map(Status::valueOf).collect(Collectors.toList());
        }
        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        Page<Event> events = repository.findAll(
                Specification.where(EventSpecification.userIdsIn(users))
                        .and(EventSpecification.statusIn(statusEnum))
                        .and(EventSpecification.categoryIdsIn(categories))
                        .and(EventSpecification.startAfter(start))
                        .and(EventSpecification.endBefore(end)),
                PageRequest.of(from, size, sortById));
        return events.isEmpty() ? new ArrayList<>() : events.getContent().stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest dto) {
        Event resultEvent = repository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Событие с ID %s не найдено\"", eventId)));
        LocalDateTime eventDate;
        if (dto.getEventDate() != null) {
            eventDate = LocalDateTime.parse(dto.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ValidationException(String.format("Поле eventDate %s должно содержать дату, которая еще не наступила.", eventDate));
            }
        }
        if (dto.getTitle() != null) {
            resultEvent.setTitle(dto.getTitle());
        }
        if (dto.getAnnotation() != null) {
            resultEvent.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            resultEvent.setDescription(dto.getDescription());
        }
        if (dto.getLocation() != null) {
            resultEvent.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        }
        if (dto.getPaid() != null) {
            resultEvent.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() > 0) {
            resultEvent.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            resultEvent.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getCategory() != null) {
            resultEvent.setCategory(categoryService.getOptionalCategoryById(dto.getCategory())
                    .orElseThrow(() -> new DataNotFoundException(String.format("Категория с ID %s не найдена",
                            dto.getCategory()))));
        }
        if ((dto.getStateAction() != null)) {
            if (!resultEvent.getState().equals(Status.PENDING)) {
                throw new ConflictException("Статус можно менять только для события в статусе ОЖИДАНИЕ ");
            }
            if ((dto.getStateAction().toString().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT.toString()))
                    && (resultEvent.getState().equals(Status.PENDING))) {
                resultEvent.setState(Status.PUBLISHED);
                resultEvent.setPublishedOn(LocalDateTime.now());
            }
            if ((dto.getStateAction().toString().equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT.toString()))
                    && (resultEvent.getState().equals(Status.PENDING))) {
                resultEvent.setState(Status.CANCELED);
            }
        }
        repository.save(resultEvent);
        log.info("Обновление администратором события : {}", resultEvent.getTitle());
        return eventMapper.toEventFullDto(resultEvent);
    }

    @Override
    public Optional<Event> findEventById(Long eventId) {
        return repository.findById(eventId);
    }

    @Override
    public void saveAfterRequest(Event event) {
        repository.save(event);
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               int from,
                                               int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if ((rangeStart != null) && (rangeEnd != null)) {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
            if (start.isAfter(end)) {
                throw new ValidationException(String.format("Указаны не валидные данные поиска: время начала %s позже времени окончания %s", rangeStart, rangeEnd));
            }
        }

        if (categories.get(0) < 1) {
            categories = null;
        }
        Sort sort1 = null;
        PageRequest pageable = PageRequest.of(from, size);
        if (sort != null && !sort.isEmpty()) {
            if (sort.equals("EVENT_DATE")) {
                sort1 = Sort.by(Sort.Direction.ASC, "eventDate");
            } else if (sort.equals("VIEWS")) {
                sort1 = Sort.by(Sort.Direction.ASC, "views");
            }
        }
        if (sort1 != null) {
            pageable = PageRequest.of(from, size, sort1);
        }
        Page<Event> events = repository.findAll(
                Specification.where(EventSpecification.isPublished())
                        .and(EventSpecification.containsText(text))
                        .and(EventSpecification.categoryIdsIn(categories))
                        .and(EventSpecification.isPaid(paid))
                        .and(EventSpecification.startAfter(start))
                        .and(EventSpecification.endBefore(end))
                        .and(EventSpecification.onlyAvailable(onlyAvailable)),
                pageable);
        return events.isEmpty() ? new ArrayList<>() : events.getContent().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByIdPublic(long eventId) {
        Event result = repository.findById(eventId).orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID %s не найдено или недоступно", eventId)));
        if (!result.getState().equals(Status.PUBLISHED)) {
            throw new DataNotFoundException(String.format("Событие с ID %s не найдено или недоступно", eventId));
        }
        EventFullDto eventFullDto = eventMapper.toEventFullDto(result);
        log.info("Найдено событие {}", result.getTitle());
        return eventFullDto;
    }

    @Override
    public List<Event> findEventByCategoryId(Long catId) {
        return repository.findByCategoryId(catId);
    }

    @Override
    public void updateConfirmationCount(Long id, long count) {
        Event event = repository.findById(id).get();
        event.setConfirmedRequests(count);
        repository.save(event);
    }
}
