package ru.practicum.ewm_main_service.comment.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentUserRequest {
    @Size(min = 1, max = 2000)
    private String text;
}
