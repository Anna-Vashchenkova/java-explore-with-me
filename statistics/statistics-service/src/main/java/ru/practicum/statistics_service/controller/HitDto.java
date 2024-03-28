package ru.practicum.statistics_service.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
