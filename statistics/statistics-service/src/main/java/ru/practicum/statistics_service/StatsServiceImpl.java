package ru.practicum.statistics_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    @Override
    public void saveHit(HitDto dto) {
        repository.save(new Hit(null, dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp()));
    }

    @Override
    public HitOutcomeDto getStat(String start, String end, ArrayList<String> uris, boolean unique) {
        ArrayList<Hit> hits = new ArrayList<>();
        if (unique == false) {
            for (String uri : uris) {
                Hit hit1 = repository.findByStartAndEndAndUri(start, end, uri);
                hits.add(hit1);
            }
        }
        else if (unique == true) {
            for (String uri : uris) {
                Hit hit2 = repository.findByStartAndEndAndUri2(start, end, uri);
                hits.add(hit2);
            }
        }
        int hitsCount = hits.size();
        return new HitOutcomeDto(hits.get(0).getApp(), hits.get(0).getUri(), hitsCount);
    }
}
