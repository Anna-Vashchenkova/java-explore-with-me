package ru.practicum.statistics_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statistics_service.exception.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void saveHit(@Valid @RequestBody HitDto dto) {
        if ((dto.getApp() == null) || (dto.getUri() == null) || (dto.getIp() == null) || (dto.getTimestamp() == null)) {
            throw new ValidationException("Входные данные не корректны");
        }
        statsService.saveHit(dto);
    }

    @GetMapping("/stats")
    public HitOutcomeDto getStat(@RequestBody String start, @RequestBody String end, @RequestBody ArrayList<String> uris, @RequestBody boolean unique) {
        if ((start.isBlank()) || (end.isBlank()) || (uris == null)) {
            throw new ValidationException("Входные данные не корректны");
        }
        return statsService.getStat(start, end, uris, unique);
    }
}