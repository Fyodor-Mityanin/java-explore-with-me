package ru.practicum.stat.server.service;

import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.client.dtos.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void createHit(EndpointHitDto endpointHitDto);

    List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
