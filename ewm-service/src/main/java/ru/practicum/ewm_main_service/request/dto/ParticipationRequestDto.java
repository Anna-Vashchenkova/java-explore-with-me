package ru.practicum.ewm_main_service.request.dto;

import lombok.*;
import ru.practicum.ewm_main_service.request.model.RequestStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
