package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.entity.dto.CategoryDto;
import ru.practicum.ewm.entity.dto.CompilationDto;
import ru.practicum.ewm.entity.dto.EventShortDto;
import ru.practicum.ewm.entity.enums.SortType;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/")
@Validated
public class PublicController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;

    public PublicController(
            CategoryService categoryService,
            CompilationService compilationService,
            EventService eventService
    ) {
        this.categoryService = categoryService;
        this.compilationService = compilationService;
        this.eventService = eventService;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return compilationService.getCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortType sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsPublic(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }



}
