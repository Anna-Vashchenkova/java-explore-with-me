package ru.practicum.ewm_main_service.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewEventDto {
    @Length(min = 3, max = 120)
    @NotBlank
    private String title;
    @Length(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @Length(min = 20, max = 7000)
    @NotBlank
    private String description;
    @NotNull
    private Long category;
    @NotNull
    @Valid
    private LocationDto location;
    @NotNull
    private String eventDate;
    private boolean paid = false;
    @PositiveOrZero
    private long participantLimit;
    private boolean requestModeration = true;
}
