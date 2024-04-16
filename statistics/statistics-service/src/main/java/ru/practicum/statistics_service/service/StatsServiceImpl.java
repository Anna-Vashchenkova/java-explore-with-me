package ru.practicum.statistics_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistics_dto.HitDto;
import ru.practicum.statistics_service.Hit;
import ru.practicum.statistics_dto.HitOutcomeDto;
import ru.practicum.statistics_service.exception.ValidationException;
import ru.practicum.statistics_service.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveHit(HitDto dto) {
        LocalDateTime dateTime = LocalDateTime.parse(dto.getTimestamp(), formatter);
        repository.save(new Hit(null, dto.getApp(), dto.getUri(), dto.getIp(), dateTime));
    }

    @Override
    public List<HitOutcomeDto> getStat(String start, String end, ArrayList<String> uris, boolean unique) {
        LocalDateTime dateTimeStart = LocalDateTime.parse(start, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(end, formatter);
        if (dateTimeStart.isAfter(dateTimeEnd)) {
            throw new ValidationException("Входные данные не корректны");
        }
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

