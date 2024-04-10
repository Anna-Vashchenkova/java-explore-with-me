package ru.practicum.ewm_main_service.request.service;

import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addNewRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto requestCancellationByRequester(Long userId, Long requestId);
}
