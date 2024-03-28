package ru.practicum.statistics_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics_service.service.StatsService;
import ru.practicum.statistics_service.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public void saveHit(@Valid @RequestBody HitDto dto) {
        if ((dto.getApp() == null) || (dto.getUri() == null) || (dto.getIp() == null) || (dto.getTimestamp() == null)) {
            throw new ValidationException("Входные данные не корректны");
        }
        LocalDateTime dateTime = LocalDateTime.parse(dto.getTimestamp(), formatter);
        statsService.saveHit(dto.getApp(), dto.getUri(), dto.getIp(), dateTime);
    }

    @GetMapping("/stats")
    public List<HitOutcomeDto> getStat(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(name = "uris", required = false) ArrayList<String> uris,
            @RequestParam(name = "unique", defaultValue = "false", required = false) boolean unique
    ) {
        if ((start.isBlank()) || (end.isBlank())) {
            throw new ValidationException("Входные данные не корректны");
        }
        LocalDateTime dateTimeStart = LocalDateTime.parse(start, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(end, formatter);
        if (dateTimeStart.isAfter(dateTimeEnd)) {
            throw new ValidationException("Входные данные не корректны");
        }
        if(!unique) {
            return statsService.getStatNotUnique(dateTimeStart, dateTimeEnd);
        } else {
            return statsService.getStatUnique(dateTimeStart, dateTimeEnd);
        }
    }
}