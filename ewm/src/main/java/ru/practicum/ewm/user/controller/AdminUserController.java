package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/admin/users")
    public List<UserDto> getAllUsers(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                     @RequestParam(name = "ids") List<Long> ids,
                                     @RequestParam(name = "from") int from,
                                   @RequestParam(name = "size") int size) {

        return userServiceImpl.getAllUsers(ownerId, ids, from, size);
    }

    @PostMapping("/admin/users")
    public UserDto addUser(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                          NewUserRequest newUserRequest) {
        return userServiceImpl.addUser(ownerId, newUserRequest);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<Object> deleteUser(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @PathVariable long userId) {
         userServiceImpl.deleteUserById(ownerId, userId);
         return ResponseEntity.ok("Пользователь удален");
    }
}
