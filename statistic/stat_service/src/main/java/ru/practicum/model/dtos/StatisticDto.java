package ru.practicum.model.dtos;

import lombok.Data;

@Data
public class StatisticDto {
    private final String app;
    private final String uri;
    private final long hits;
}
