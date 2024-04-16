package ru.practicum.ewm_main_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.DataAlreadyExists;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm_main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.model.RequestStatus;
import ru.practicum.ewm_main_service.request.service.RequestService;
import ru.practicum.statistics_client.StatisticsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final RequestService requestService;
    private final StatisticsClient client;

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
                                    @Valid @RequestBody UpdateEventUserRequest dto) {
        log.info("Получен запрос на обновление информации о событии с ID {}, добавленном пользователем с ID {}", eventId, userId);
        return eventService.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsForOwnEvent(@PathVariable(name = "userId") Long userId,
                                                                 @PathVariable(name = "eventId") Long eventId) {
        log.info("Запрос на получение информации о чужих запросах на участие в собственном событии с ID {} от пользователя с ID {}", eventId, userId);
        return requestService.findRequestsForOwnEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable(name = "userId") Long userId,
                                                         @PathVariable(name = "eventId") Long eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest dto) {
        log.info("Запрос на изменение статуса заявок на участие в событии с ID= {} текущего пользователя ID= {}", eventId, userId);
        if (RequestStatus.valueOf(dto.getStatus()) == null) {
            throw new DataAlreadyExists(String.format("Статус заявок %s не валидный", dto.getStatus()));
        }
        return requestService.updateRequests(userId, eventId, dto);
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
                                           @Valid @RequestBody UpdateEventAdminRequest dto) {
        log.info("Получен запрос на обновление администратором информации о событии с ID {}", eventId);
        return eventService.updateEventByAdmin(eventId, dto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventsPublic(@RequestParam(name = "text", required = false) String text,
                                               @RequestParam(name = "categories", required = false) List<Long> categories,
                                               @RequestParam(name = "paid", required = false) Boolean paid,
                                               @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                               @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                               @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(name = "sort", required = false) String sort,
                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               HttpServletRequest request) {
        log.info("Получен запрос на получение информации об опубликованных событиях по фильтрам");
        List<EventShortDto> eventsPublic = eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        client.saveHit("ewm-service", request.getRemoteHost(), request.getRequestURI(),
                LocalDateTime.now());
        return eventsPublic;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable(name = "id") long eventId,
                                           HttpServletRequest request) {
        log.info("Получен запрос на получение подробной информации об опубликованном событии по его идентификатору {}", eventId);
        EventFullDto fullDto = eventService.getEventByIdPublic(eventId);
        client.saveHit("ewm-service", request.getRemoteHost(), request.getRequestURI(),
                LocalDateTime.now());
        return fullDto;
    }
}
