package ru.practicum.statistics_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface StatsRepository extends  JpaRepository<Hit, Long> {
    @Query("select Hit from Hit where Hit.uri = :uri ")
    Hit findByStartAndEndAndUri(String start, String end, String uri);

    @Query("select Hit from Hit where Hit.uri = :uri order by avg (Hit.ip)")
    Hit findByStartAndEndAndUri2(String start, String end, String uri);
}
