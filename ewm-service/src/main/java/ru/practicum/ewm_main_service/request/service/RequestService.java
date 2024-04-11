package ru.practicum.ewm_main_service.request.service;

import ru.practicum.ewm_main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm_main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addNewRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto requestCancellationByRequester(Long userId, Long requestId);

    List<ParticipationRequestDto> findRequestsForOwnEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest dto);
}
