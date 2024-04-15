package ru.practicum.statistics_client;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.statistics_dto.HitDto;
import ru.practicum.statistics_dto.HitOutcomeDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class StatisticsClient {
    private final WebClient webClient;
    private String baseUrl;
    private static final String HIT_URI = "/hit";
    private static final String STAT_URI = "/stats";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveHit(String app, String uri, String ip, LocalDateTime dateTime) {
        webClient.post()
                .uri(baseUrl + HIT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new HitDto(app, uri, ip, dateTime.format(formatter)))
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public Mono<List<HitOutcomeDto>> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if ((start == null) || (end == null)) {
            throw new IllegalArgumentException("Неверные параметры запроса");
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(baseUrl + STAT_URI)
                        .queryParam("start", start.format(formatter))
                        .queryParam("end", end.format(formatter))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError,
                        clientResponse -> Mono.error(new StatisticsException("Невозможно получить статистику")))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
