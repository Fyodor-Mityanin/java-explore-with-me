package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dtos.EndpointHitDto;
import ru.practicum.model.dtos.StatisticDto;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@Slf4j
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> hitEndpoint(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statService.createHit(endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public List<StatisticDto> getStat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        return statService.getStatistic(start, end, uris, unique);
    }


}
