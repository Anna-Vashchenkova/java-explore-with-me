package ru.practicum.ewm_main_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable(name = "userId") long userId,
                                    @Valid @RequestBody NewEventDto dto) {
        log.info("Получен запрос на добавление нового события {} пользователем с ID {}", dto.getTitle(), userId);
        EventFullDto eventFullDto = eventService.addNewEvent(userId, dto);
        log.info("Создано событие с ID={}", eventFullDto.getId());
        return eventFullDto;
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

    @PatchMapping("users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "userId") long userId,
                                    @PathVariable(name = "eventId") long eventId,
                                    @RequestBody UpdateEventUserRequest dto) {
        log.info("Получен запрос на обновление информации о событии с ID {}, добавленном пользователем с ID {}", eventId, userId);
        return eventService.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsForOwnEvent(@PathVariable(name = "userId", required = true) Long userId,
                                                         @PathVariable(name = "eventId", required = true) Long eventId) {
        log.info("Запрос на получение информации о чужих запросах на участие в собственном событии с ID {} от пользователя с ID {}", eventId, userId);
        return requestService.findRequestsForOwnEvent(userId, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> searchEvent(@RequestParam(required = false) List<Long> users,
                                          @RequestParam(required = false) List<String> states,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(name = "from", defaultValue = "0") int from,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен запрос на поиск событий по фильтрам ");
        return eventService.searchEvent(users, states, categories, rangeStart, rangeEnd, from / size, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable(name = "eventId") long eventId,
                                           @RequestBody UpdateEventAdminRequest dto) {
        log.info("Получен запрос на обновление администратором информации о событии с ID {}", eventId);
        return eventService.updateEventByAdmin(eventId, dto);
    }
}
