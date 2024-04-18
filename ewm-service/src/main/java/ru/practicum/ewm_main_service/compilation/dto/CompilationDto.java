package ru.practicum.ewm_main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CompilationDto {
    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank(message = "Название подборки не может быть пустым")
    private String title;
    private List<EventShortDto> events;
}
