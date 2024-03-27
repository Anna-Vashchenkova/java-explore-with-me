package ru.practicum.statistics_service.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HitOutcomeDto {
    String app;
    String uri;
    int hitsCount;
}