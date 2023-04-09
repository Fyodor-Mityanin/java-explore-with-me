package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.UserDto;
import ru.practicum.ewm.entity.dto.UserRequestDto;
import ru.practicum.ewm.entity.dto.UserShortDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto toDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .build();
    }

    public static UserShortDto toShortDto(User u) {
        return UserShortDto.builder()
                .id(u.getId())
                .name(u.getName())
                .build();
    }

    public static List<UserDto> toDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        users.forEach(user -> dtos.add(toDto(user)));
        return dtos;
    }

    public static User toObject(UserRequestDto u) {
        User user = new User();
        user.setName(u.getName());
        user.setEmail(u.getEmail());
        return user;
    }
}
