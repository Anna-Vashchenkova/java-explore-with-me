package ru.practicum.statistics_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HitOutcomeDto {
    private String app;
    private String uri;
    private long hits;
}