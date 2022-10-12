package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUserById(long userId);
}
