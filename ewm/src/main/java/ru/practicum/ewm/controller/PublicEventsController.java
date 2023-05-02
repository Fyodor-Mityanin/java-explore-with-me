package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.CommentDto;
import ru.practicum.ewm.entity.dto.EventFullDto;
import ru.practicum.ewm.entity.dto.EventShortDto;
import ru.practicum.ewm.entity.enums.SortType;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@Validated
public class PublicEventsController {
    private final EventService eventService;
    private final StatisticService statisticService;

    @Autowired
    public PublicEventsController(EventService eventService, StatisticService statisticService) {
        this.eventService = eventService;
        this.statisticService = statisticService;
    }

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortType sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        statisticService.hit(request);
        return eventService.getEventsPublic(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventsId}")
    public EventFullDto getEvent(@PathVariable Long eventsId, HttpServletRequest request) {
        statisticService.hit(request);
        return eventService.getEventById(eventsId);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getComments(@PathVariable Long eventId) {
        return eventService.getCommentsByEventId(eventId);
    }
}
