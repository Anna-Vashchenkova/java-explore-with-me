package ru.practicum.statistics_service.service;

import ru.practicum.statistics_service.controller.HitOutcomeDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface StatsService {
    void saveHit(String app, String uri, String ip, LocalDateTime dateTime);

    HitOutcomeDto getStat(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, boolean unique);
}
