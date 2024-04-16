package ru.practicum.ewm_main_service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("Получен запрос от пользователя {} на участие в событии с ID {}", userId, eventId);
        return requestService.addNewRequest(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Получен запрос на получение информации о запросах, добавленных пользователем с ID {}", userId);
        return requestService.getRequests(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto requestCancellationByRequester(@PathVariable Long userId,
                                                                  @PathVariable Long requestId) {
        log.info("Получен запрос на отмену своего запроса с ID {} на участие от пользователя с ID {}", requestId, userId);
        return requestService.requestCancellationByRequester(userId, requestId);
    }
}
