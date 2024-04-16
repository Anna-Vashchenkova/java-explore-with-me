package ru.practicum.statistics_service.service;

import ru.practicum.statistics_dto.HitDto;
import ru.practicum.statistics_dto.HitOutcomeDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface StatsService {
    void saveHit(HitDto dto);

    List<HitOutcomeDto> getStatNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<HitOutcomeDto> getStatUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<HitOutcomeDto> getStat(String start, String end, ArrayList<String> uris, boolean unique);
}
