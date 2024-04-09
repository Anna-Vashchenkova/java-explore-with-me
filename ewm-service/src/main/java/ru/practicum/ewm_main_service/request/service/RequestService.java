package ru.practicum.ewm_main_service.request.service;

import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;

public interface RequestService {
    ParticipationRequestDto addNewRequest(Long userId, Long eventId);
}
