package ru.practicum.ewm_main_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.NewEventDto;
import ru.practicum.ewm_main_service.event.service.EventService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable(name = "userId") long userId,
                                    @Valid @RequestBody NewEventDto dto) {
        return eventService.addNewEvent(userId, dto);

    }
}
