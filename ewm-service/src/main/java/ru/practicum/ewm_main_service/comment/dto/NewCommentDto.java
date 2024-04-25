package ru.practicum.ewm_main_service.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewCommentDto {
    @Size(min = 1, max = 2000)
    @NotBlank
    private String text;
}