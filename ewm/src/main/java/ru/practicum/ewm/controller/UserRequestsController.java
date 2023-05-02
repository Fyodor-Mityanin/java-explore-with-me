package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.ParticipationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserRequestsController {

    private final ParticipationService participationService;

    @Autowired
    public UserRequestsController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipation(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationService.createParticipation(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipation(@PathVariable Long userId) {
        return participationService.getParticipationsByUserId(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipation(@PathVariable Long userId, @PathVariable Long requestId) {
        return participationService.cancelParticipation(userId, requestId);
    }
}
