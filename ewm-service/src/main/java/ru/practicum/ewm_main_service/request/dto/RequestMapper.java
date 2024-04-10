package ru.practicum.ewm_main_service.request.dto;

import ru.practicum.ewm_main_service.request.model.Request;

import java.time.format.DateTimeFormatter;

public final class RequestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toDto(Request entity) {
        return ParticipationRequestDto.builder()
                .id(entity.getId())
                .requester(entity.getRequester().getId())
                .created(entity.getCreated().format(formatter))
                .event(entity.getEvent().getId())
                .status(entity.getStatus())
                .build();
    }
}
