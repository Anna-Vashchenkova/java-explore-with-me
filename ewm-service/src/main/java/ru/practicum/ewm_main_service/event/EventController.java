package ru.practicum.ewm_main_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public EventFullDto addNewEvent(@PathVariable(name = "userId") long userId,
                                    @Valid @RequestBody NewEventDto dto) {
        return eventService.addNewEvent(userId, dto);

    }
}
