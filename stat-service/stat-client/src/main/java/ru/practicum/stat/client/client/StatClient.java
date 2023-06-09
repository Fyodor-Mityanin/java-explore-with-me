package ru.practicum.stat.client.client;

import org.springframework.http.ResponseEntity;
import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.client.dtos.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatClient {

    ResponseEntity<Void> hit(EndpointHitDto endpointHitDto, String serviceUrl);

    List<StatisticDto> stats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, String serviceUrl);
}
