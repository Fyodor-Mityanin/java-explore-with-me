package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.EventService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@Validated
public class AdminCommentsController {

    private final EventService eventService;

    @Autowired
    public AdminCommentsController(EventService eventService) {
        this.eventService = eventService;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long commentId) {
        eventService.deleteCommentAdmin(commentId);
    }
}
