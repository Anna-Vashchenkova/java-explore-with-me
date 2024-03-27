package ru.practicum.statistics_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics_service.Hit;
import ru.practicum.statistics_service.controller.HitOutcomeDto;
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
    @Transactional(readOnly = true)
    public HitOutcomeDto getStat(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, boolean unique) {
        ArrayList<Hit> hits = new ArrayList<>();
        int hitsCount = 0;
        Hit hit = null;
        if (uris != null) {
            if (unique == false) {
                for (String uri : uris) {
                    ArrayList<Hit> hits1 = repository.findByStartAndEndAndUri(start, end, uri);
                    hitsCount += hits1.size();
                    hit = hits1.get(0);
                }
            }
            else if (unique == true) {
                for (String uri : uris) {
                    ArrayList<Hit> hits2 = repository.findByStartAndEndAndUri2(start, end, uri);
                    hitsCount += hits2.size();
                    hit = hits2.get(0);
                }
            }
            return new HitOutcomeDto(hit.getApp(), hit.getUri(), hitsCount);
        } else {
            ArrayList<Hit> allByStartAndEnd = repository.findAllByStartAndEnd(start, end);
            hitsCount += allByStartAndEnd.size();
            hit = allByStartAndEnd.get(0);
            return new HitOutcomeDto(hit.getApp(), hit.getUri(), hitsCount);
        }
    }
}

