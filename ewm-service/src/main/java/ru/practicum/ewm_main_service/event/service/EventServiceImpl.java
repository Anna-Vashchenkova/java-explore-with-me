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
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addNewEvent(long userId, NewEventDto dto) {
        Category category = categoryService.get(dto.getCategory());
        LocalDateTime eventDate = LocalDateTime.parse(dto.getEventDate(), formatter);
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataAlreadyExists(String.format("Поле eventDate %s должно содержать дату, которая еще не наступила.", eventDate));
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
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Page<Event> allByUserId = repository.findAllByUserId(userId, PageRequest.of(from, size, sortById));
        return allByUserId.isEmpty() ? new ArrayList<>() : allByUserId.getContent().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(long userId, long eventId) {
        Event event = repository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Событие с ID %s, созданное пользователем с ID %s, не найдено", eventId, userId)));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto) {
        Event resultEvent = repository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Событие с ID %s, созданное пользователем с ID %s, не найдено\"", eventId, userId)));
        Event eventUpdate = EventMapper.toUpdateEvent(dto);
        LocalDateTime eventDate;
        if (dto.getEventDate() != null) {
            eventDate = LocalDateTime.parse(dto.getEventDate(), formatter);
            if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataAlreadyExists(String.format("Поле eventDate %s должно содержать дату, которая еще не наступила.", eventDate));
            }
        }
        if (dto.getCategory() != null) {
            eventUpdate.setCategory(categoryService.getOptionalCategoryById(dto.getCategory())
                    .orElseThrow(() -> new DataNotFoundException(String.format("Категория с ID %s не найдена",
                            dto.getCategory()))));
        }
        if (resultEvent.getState().equals(Status.PUBLISHED)) {
            throw new DataAlreadyExists("Событие не должно быть опубликовано");
        }
        if (dto.getPaid() != null) {
            resultEvent.setPaid(eventUpdate.isPaid());
        }
        if (dto.getRequestModeration() != null) {
            resultEvent.setRequestModeration(eventUpdate.isRequestModeration());
        }
        setFields(resultEvent, eventUpdate);

        if (dto.getStateAction().toString().equals(UpdateEventUserRequest.StateAction.CANCEL_REVIEW.toString())) {
            resultEvent.setState(Status.CANCELED);
        } else if (UpdateEventUserRequest.StateAction.SEND_TO_REVIEW.toString().equals(dto.getStateAction().toString())) {
            resultEvent.setState(Status.PENDING);
        }
        repository.save(resultEvent);
        log.info("Update event: {}", resultEvent.getTitle());
        return EventMapper.toEventFullDto(resultEvent);

    }

    @Override
    public List<EventFullDto> searchEvent(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        if (users.get(0) < 1) {
            users = null;
        }
        if (categories.get(0) < 1) {
            categories = null;
        }

        List<Status> statusEnum = null;
        if (states != null) {
            statusEnum = states.stream().map(Status::valueOf).filter(Objects::nonNull).collect(Collectors.toList());
        }
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
       /* Page<Event> byParams = repository.findByParams(users, statusEnum, categories, start, end, PageRequest.of(from, size, sortById));
        return byParams.isEmpty() ? new ArrayList<>() : byParams.getContent().stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());*/
        Page<Event> events = repository.findAll(
                Specification.where(EventSpecification.userIdsIn(users))
                        .and(EventSpecification.statusIn(statusEnum))
                        .and(EventSpecification.categoryIdsIn(categories))
                        .and(EventSpecification.startAfter(start))
                        .and(EventSpecification.endBefore(end)),
                PageRequest.of(from, size, sortById));
        return events.isEmpty() ? new ArrayList<EventFullDto>() : events.getContent().stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    private void setFields(Event resultEvent, Event eventUpdate) {
        if (eventUpdate.getTitle() != null) {
            resultEvent.setTitle(eventUpdate.getTitle());
        }
        if (eventUpdate.getAnnotation() != null) {
            resultEvent.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getDescription() != null) {
            resultEvent.setDescription(eventUpdate.getDescription());
        }
        if (eventUpdate.getCategory() != null) {
            resultEvent.setCategory(eventUpdate.getCategory());
        }
        if (eventUpdate.getLocation() != null) {
            resultEvent.setLocation(eventUpdate.getLocation());
        }
        if (eventUpdate.getEventDate() != null) {
            resultEvent.setEventDate(eventUpdate.getEventDate());
        }
        if (eventUpdate.getParticipantLimit() != 0) {
            resultEvent.setParticipantLimit(eventUpdate.getParticipantLimit());
        }
    }
}
