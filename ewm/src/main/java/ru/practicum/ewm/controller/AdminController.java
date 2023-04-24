package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.*;
import ru.practicum.ewm.entity.enums.State;
import ru.practicum.ewm.error.exeptions.BadRequestException;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
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
    private final CompilationService compilationService;

    @Autowired
    public AdminController(
            UserService userService,
            CategoryService categoryService,
            EventService eventService,
            CompilationService compilationService
    ) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
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
        if (userRequestDto.getName() == null) {
            throw new BadRequestException("UserRequestDto is empty");
        }
        return userService.createUser(userRequestDto);
    }

    @DeleteMapping("/users/{usersId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long usersId) {
        userService.deleteUser(usersId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
            @PathVariable Long eventId
    ) {
        return eventService.patchEventAdmin(updateEventAdminRequest, eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsAdmin(users, states, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null) {
            throw new BadRequestException("NewCompilationDto.title is empty");
        }
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compilationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compilationId) {
        compilationService.deleteById(compilationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/compilations/{compilationId}")
    public CompilationDto updateCompilation(
            @PathVariable Long compilationId,
            @Valid @RequestBody UpdateCompilationRequest request
    ) {
        return compilationService.updateCompilation(compilationId, request);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new BadRequestException("NewCategoryDto.name is empty");
        }
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long catId) {
        if (categoryDto.getId() == null && categoryDto.getName() == null) {
            throw new BadRequestException("CategoryDto is empty");
        }
        return categoryService.patchCategory(categoryDto, catId);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }
}
