package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.*;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.ParticipationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final ParticipationService participationService;

    private final EventService eventService;

    @Autowired
    public UserController(ParticipationService participationService, EventService eventService) {
        this.participationService = participationService;
        this.eventService = eventService;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipation(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationService.createParticipation(userId, eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @PathVariable Long userId) {
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