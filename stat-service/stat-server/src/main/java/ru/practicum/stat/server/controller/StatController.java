package ru.practicum.stat.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.client.dtos.StatisticDto;
import ru.practicum.stat.server.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@Slf4j
public class StatController {
    private final StatService statService;

    private final String dateTimePattern = "[yyyy-MM-dd'%20'HH:mm:ss][yyyy-MM-dd HH:mm:ss]";

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hitEndpoint(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statService.createHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatisticDto> getStat(
            @RequestParam @DateTimeFormat(pattern = dateTimePattern) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = dateTimePattern) LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        return statService.getStatistic(start, end, uris, unique);
    }


}
