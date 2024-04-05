package ru.practicum.ewm_main_service.event.service;

import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.event.dto.NewEventDto;
import ru.practicum.ewm_main_service.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(long userId, NewEventDto dto);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);
}
