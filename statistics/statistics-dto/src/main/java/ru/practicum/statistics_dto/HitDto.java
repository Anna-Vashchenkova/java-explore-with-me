package ru.practicum.statistics_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class HitDto {
    @NotBlank
    private String ip;
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String timestamp;
}
