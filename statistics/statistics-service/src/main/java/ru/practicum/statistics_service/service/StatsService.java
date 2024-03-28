package ru.practicum.statistics_service.service;

import ru.practicum.statistics_service.controller.HitOutcomeDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(String app, String uri, String ip, LocalDateTime dateTime);

    List<HitOutcomeDto> getStatNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<HitOutcomeDto> getStatUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
