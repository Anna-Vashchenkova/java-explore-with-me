package ru.practicum.statistics_service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HitDto {
    String app;
    String uri;
    String ip;
    String timestamp;
}
