package ru.practicum.statistics_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics_service.Hit;
import ru.practicum.statistics_service.controller.HitOutcomeDto;
import ru.practicum.statistics_service.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    @Override
    public void saveHit(String app, String uri, String ip, LocalDateTime dateTime) {
        repository.save(new Hit(null, app, uri, ip, dateTime));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HitOutcomeDto> getStatNotUnique(LocalDateTime start, LocalDateTime end) {
        return repository.findByStartAndEnd(start, end);
    }

    @Override
    public List<HitOutcomeDto> getStatUnique(LocalDateTime start, LocalDateTime end) {
        return repository.findByStartAndEndAndIp(start, end);
    }
}

