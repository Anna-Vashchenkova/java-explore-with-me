package ru.practicum.statistics_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics_dto.HitDto;
import ru.practicum.statistics_dto.HitOutcomeDto;
import ru.practicum.statistics_service.service.StatsService;
import ru.practicum.statistics_service.exception.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody HitDto dto) {
        statsService.saveHit(dto);
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
        return statsService.getStat(start, end, uris, unique);
    }
}