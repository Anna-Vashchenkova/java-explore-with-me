package ru.practicum.ewm_main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.category.service.CategoryService;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.EventMapper;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.event.dto.NewEventDto;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.event.model.Location;
import ru.practicum.ewm_main_service.event.model.Status;
import ru.practicum.ewm_main_service.event.repository.EventRepository;
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
import ru.practicum.ewm_main_service.exception.DataNotFoundException;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
            throw new DataAlreadyExists("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + eventDate);
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
}
