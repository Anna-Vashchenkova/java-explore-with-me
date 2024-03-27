package ru.practicum.statistics_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistics_service.Hit;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface StatsRepository extends  JpaRepository<Hit, Long> {
    @Query("select h from Hit as h where h.uri = :uri and h.timestamp >= :start and h.timestamp <= :end ")
    ArrayList<Hit> findByStartAndEndAndUri(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select h from Hit as h where h.uri = :uri and h.timestamp >= :start and h.timestamp <= :end ")
    ArrayList<Hit> findByStartAndEndAndUri2(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select h from Hit as h where h.timestamp >= :start and h.timestamp <= :end ")
    ArrayList<Hit> findAllByStartAndEnd(LocalDateTime start, LocalDateTime end);
}
