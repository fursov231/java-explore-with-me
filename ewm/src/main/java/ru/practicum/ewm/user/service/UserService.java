package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
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
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(long ownerId, List<Long> ids, int from, int size) {
        Optional<User> userOptional = userRepository.findById(ownerId);
        if (userOptional.isPresent()) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            Page<User> userPage = userRepository.findAll(pageRequest);
            List<User> targetUsers = new ArrayList<>();
            if (!ids.isEmpty()) {
                for (var id : ids) {
                    Optional<User> user = userRepository.findById(id);
                    user.ifPresent(targetUsers::add);
                }
            }
                if (!targetUsers.isEmpty()) {
                    return userPage.getContent()
                            .stream()
                            .filter(targetUsers::contains)
                            .map(UserMapper::toDto)
                            .collect(Collectors.toList());
                } else {
                    return userPage.getContent()
                            .stream()
                            .map(UserMapper::toDto)
                            .collect(Collectors.toList());
                }
            }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Transactional
    public UserDto addUser(long ownerId, NewUserRequest newUserRequest) {
        Optional<User> userOptional = userRepository.findById(ownerId);
        if (userOptional.isPresent()) {
            return UserMapper.toDto(userRepository.save(UserMapper.requestDtoToUser(newUserRequest)));
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Transactional
    public void deleteUserById(long ownerId, long userId) {
        Optional<User> userOptional = userRepository.findById(ownerId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }


}
