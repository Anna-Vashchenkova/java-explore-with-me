package ru.practicum.statistics_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistics_service.Hit;
import ru.practicum.statistics_service.controller.HitOutcomeDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends  JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.statistics_service.controller.HitOutcomeDto(h.app, h.uri, count(h.id)) from Hit as h " +
    "where h.timestamp >= :start and h.timestamp <= :end group by h.app, h.uri order by count(h.id) desc ")
    List<HitOutcomeDto> findByStartAndEnd(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statistics_service.controller.HitOutcomeDto(h.app, h.uri, count(h.id)) from Hit as h " +
            "where h.timestamp >= :start and h.timestamp <= :end and h.uri in :uris group by h.app, h.uri order by count(h.id) desc")
    List<HitOutcomeDto> findByStartAndEndAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.statistics_service.controller.HitOutcomeDto(h.app, h.uri, count(distinct(h.ip))) from Hit as h " +
            "where h.timestamp >= :start and h.timestamp <= :end group by h.app, h.uri order by count(distinct(h.ip)) desc ")
    List<HitOutcomeDto> findByStartAndEndAndIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statistics_service.controller.HitOutcomeDto(h.app, h.uri, count(distinct(h.ip))) from Hit as h " +
            "where h.timestamp >= :start and h.timestamp <= :end and h.uri in :uris group by h.app, h.uri order by count(distinct(h.ip)) desc")
    List<HitOutcomeDto> findByStartAndEndAndUriAndIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
