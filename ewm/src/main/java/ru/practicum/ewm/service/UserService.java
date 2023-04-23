package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.UserDto;
import ru.practicum.ewm.entity.dto.UserRequestDto;
import ru.practicum.ewm.entity.mapper.UserMapper;
import ru.practicum.ewm.error.exeptions.ConflictException;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsersPageable(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<User> users = userRepository.findAll(pageable);
        return UserMapper.toDtos(users.toList());
    }

    public List<UserDto> getUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        return UserMapper.toDtos(users);
    }

    public UserDto createUser(UserRequestDto userRequestDto) {
        userRepository.findByNameIgnoreCase(userRequestDto.getName()).ifPresent(
                cat -> {
                    throw new ConflictException("User with name=" + userRequestDto.getName() + " already exist");
                }
        );
        User user = UserMapper.toObject(userRequestDto);
        user = userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public void deleteUser(Long usersId) {
        userRepository.deleteById(usersId);
    }
}
