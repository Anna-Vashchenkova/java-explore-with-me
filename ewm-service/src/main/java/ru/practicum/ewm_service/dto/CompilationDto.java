package ru.practicum.ewm_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class CompilationDto {
    List<EventShortDto> events;
    @NotNull
    Long id;
    @NotNull
    Boolean pinned;
    @NotBlank(message = "Название не должно быть пустым")
    String title;
}
