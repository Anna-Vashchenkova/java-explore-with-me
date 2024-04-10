package ru.practicum.ewm_main_service.event.service;

import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventFullDto addNewEvent(long userId, NewEventDto dto);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);

    List<EventFullDto> searchEvent(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest dto);

    Optional<Event> findEventById(Long eventId);

    void saveAfterRequest(Event event);
}
