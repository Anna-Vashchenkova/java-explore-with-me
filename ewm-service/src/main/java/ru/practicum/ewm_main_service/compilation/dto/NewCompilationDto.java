package ru.practicum.ewm_main_service.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewCompilationDto {
    private boolean pinned = false;
    @NotBlank(message = "Название подборки не может быть пустым")
    @Length(min = 1, max = 50)
    private String title;
    private List<Long> events;
}
