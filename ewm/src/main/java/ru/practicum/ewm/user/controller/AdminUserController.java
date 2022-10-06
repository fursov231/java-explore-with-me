package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> getAllUsers(@RequestParam(name = "ids") List<Long> ids,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping("/admin/users")
    public UserDto addUser(NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("Пользователь удален");
    }
}
