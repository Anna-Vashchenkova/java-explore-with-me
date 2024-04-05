package ru.practicum.ewm_main_service.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequest {
    @Length(min = 3, max = 120)
    @NotBlank
    private String title;
    @Length(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @Length(min = 20, max = 7000)
    @NotBlank
    private String description;
    private Long category;
    @Valid
    private LocationDto location;
    private String eventDate;
    private Boolean paid;
    @PositiveOrZero
    private long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }
}
