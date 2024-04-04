package ru.practicum.ewm_main_service.event.service;

import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.NewEventDto;

public interface EventService {
    EventFullDto addNewEvent(long userId, NewEventDto dto);
}
