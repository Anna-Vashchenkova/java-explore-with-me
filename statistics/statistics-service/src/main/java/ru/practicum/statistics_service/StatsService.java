package ru.practicum.statistics_service;

import java.util.ArrayList;

public interface StatsService {
    void saveHit(HitDto dto);

    HitOutcomeDto getStat(String start, String end, ArrayList<String> uris, boolean unique);
}
