package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.*;
import ru.practicum.ewm.entity.enums.State;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@Validated
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;

    @Autowired
    public AdminController(UserService userService, CategoryService categoryService, EventService eventService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        if (ids == null) {
            return userService.getUsersPageable(from, size);
        } else {
            return userService.getUsersByIds(ids);
        }
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto patchEvent(
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
            @PathVariable Long eventId
    ) {
        return eventService.patchEvent(updateEventAdminRequest, eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> userIds,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEvents(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }


}
