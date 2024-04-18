package ru.practicum.ewm_main_service.comment.dto;

import lombok.*;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    @NotBlank(message = "Поле не может быть пустым.")
    private String text;
    @NotNull(message = "Поле owner не может быть пустым.")
    private UserShortDto owner;
    @NotNull(message = "Поле event не может быть пустым.")
    private EventShortDto event;
    private String createdOn;
}
