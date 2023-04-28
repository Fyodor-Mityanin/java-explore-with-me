package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.*;
import ru.practicum.ewm.error.exeptions.BadRequestException;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.ParticipationService;
import ru.practicum.ewm.service.StatisticService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserEventsController {
    private final ParticipationService participationService;
    private final EventService eventService;
    private final StatisticService statisticService;

    @Autowired
    public UserEventsController(
            ParticipationService participationService,
            EventService eventService,
            StatisticService statisticService
    ) {
        this.participationService = participationService;
        this.eventService = eventService;
        this.statisticService = statisticService;
    }


    @GetMapping("/{userId}/events/{eventId}/requests")
    @SuppressWarnings("unused")
    public List<ParticipationRequestDto> getEventRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return participationService.getParticipationsByUserIdAndEventId(eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @PathVariable Long userId) {
        if (newEventDto.getAnnotation() == null || newEventDto.getDescription() == null) {
            throw new BadRequestException("NewEventDto.annotation is empty");
        }
        return eventService.createEvent(newEventDto, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @SuppressWarnings("unused")
    public EventFullDto getEvents(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            HttpServletRequest request
    ) {
        statisticService.hit(request);
        return eventService.getEventById(eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventStatus(
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return participationService.updateRequestStatus(eventRequestStatusUpdateRequest, userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchEvent(
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventService.patchEventUser(updateEventUserRequest, userId, eventId);
    }
}
