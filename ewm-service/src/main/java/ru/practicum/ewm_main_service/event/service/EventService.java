package ru.practicum.ewm_main_service.event.service;

import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventFullDto addNewEvent(long userId, NewEventDto dto);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);

    List<EventFullDto> searchEvent(List<Long> users,
                                   List<String> states,
                                   List<Long> categories,
                                   String rangeStart,
                                   String rangeEnd,
                                   int from,
                                   int size);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest dto);

    Optional<Event> findEventById(Long eventId);

    void saveAfterRequest(Event event);

    List<EventShortDto> getEventsPublic(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        int from,
                                        int size);

    EventFullDto getEventByIdPublic(long eventId);

    List<Event> findEventByCategoryId(Long catId);

    void updateConfirmationCount(Long id, long count);
}
