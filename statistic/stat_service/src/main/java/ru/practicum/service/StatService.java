package ru.practicum.service;

import ru.practicum.model.dtos.EndpointHitDto;
import ru.practicum.model.dtos.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void createHit(EndpointHitDto endpointHitDto);

    List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
