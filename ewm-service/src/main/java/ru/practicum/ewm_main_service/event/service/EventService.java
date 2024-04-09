package ru.practicum.ewm_main_service.event.service;

import ru.practicum.ewm_main_service.event.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(long userId, NewEventDto dto);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);

    List<EventFullDto> searchEvent(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest dto);
}
