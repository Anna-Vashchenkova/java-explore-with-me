package ru.practicum.statistics_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistics_service.Hit;
import ru.practicum.statistics_dto.HitOutcomeDto;
import ru.practicum.statistics_service.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<HitOutcomeDto> getStat(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, ArrayList<String> uris, boolean unique) {
        if (!unique) {
            return getStatNotUnique(dateTimeStart, dateTimeEnd, uris);
        } else {
            return getStatUnique(dateTimeStart, dateTimeEnd, uris);
        }
    }

    @Override
    public List<HitOutcomeDto> getStatNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null) {
            return repository.findByStartAndEnd(start, end);
        } else {
            return repository.findByStartAndEndAndUri(start, end, uris);
        }
    }

    @Override
    public List<HitOutcomeDto> getStatUnique(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null) {
            return repository.findByStartAndEndAndIp(start, end);
        } else {
            return repository.findByStartAndEndAndUriAndIp(start, end, uris);
        }
    }
}

