package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.dto.UserDto;
import ru.practicum.ewm.entity.dto.UserRequestDto;
import ru.practicum.ewm.error.exeptions.BadRequestException;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUsersController {

    private final UserService userService;

    @Autowired
    public AdminUsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        if (userRequestDto.getName() == null) {
            throw new BadRequestException("UserRequestDto is empty");
        }
        return userService.createUser(userRequestDto);
    }

    @DeleteMapping("/{usersId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long usersId) {
        userService.deleteUser(usersId);
    }
}
