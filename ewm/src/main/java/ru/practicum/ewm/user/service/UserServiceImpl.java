package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.util.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<User> userPage = userRepository.findAll(pageRequest);
        if (!ids.isEmpty()) {
            List<User> foundUsers = new ArrayList<>();
            for (var id : ids) {
                Optional<User> user = userRepository.findById(id);
                user.ifPresent(foundUsers::add);
            }
            return foundUsers.stream().map(UserMapper::toDto).collect(Collectors.toList());
        }
        return userPage.getContent().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        log.info("Пользователь name=${} добавлен", newUserRequest.getName());
        return UserMapper.toDto(userRepository.save(UserMapper.requestDtoToUser(newUserRequest)));
    }

    @Transactional
    public void deleteUserById(long userId) {
        log.info("Пользователь id=${} удален", userId);
        userRepository.deleteById(userId);
    }
}
