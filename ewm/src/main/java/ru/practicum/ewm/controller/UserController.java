package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.ParticipationService;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final ParticipationService participationService;

    @Autowired
    public UserController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createParticipation(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationService.createParticipation(userId, eventId);
    }


}
