package ru.practicum.ewm_main_service.request.dto;

import ru.practicum.ewm_main_service.request.model.Request;

public class RequestMapper {
    public static ParticipationRequestDto toDto(Request entity) {
        return ParticipationRequestDto.builder()
                .id(entity.getId())
                .requester(entity.getRequester().getId())
                .created(entity.getCreated())
                .event(entity.getEvent().getId())
                .status(entity.getStatus())
                .build();
    }
}
