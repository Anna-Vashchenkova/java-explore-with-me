package ru.practicum.ewm_main_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.event.dto.NewEventDto;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable(name = "userId") long userId,
                                    @Valid @RequestBody NewEventDto dto) {
        log.info("Получен запрос на добавление нового события {} пользователем с ID {}", dto.getTitle(), userId);
        return eventService.addNewEvent(userId, dto);
    }

    @GetMapping("users/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable(name = "userId") long userId,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен запрос на получение информации о событиях, добавленных пользователем с ID {} по {} элементов на странице {}", userId, size, from);
        if ((from < 0) || (size < 1)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable(name = "userId") long userId,
                                     @PathVariable(name = "eventId") long eventId) {
        log.info("Получен запрос на получение информации о событии с ID {}, добавленном пользователем с ID {}", eventId, userId);
        return eventService.getEventById(userId, eventId);
    }

}
