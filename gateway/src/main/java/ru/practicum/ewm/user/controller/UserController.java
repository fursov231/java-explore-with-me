package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.client.UserAdminClient;
import ru.practicum.ewm.user.dto.NewUserRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserAdminClient adminClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminClient.getAllUsers(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                          NewUserRequest newUserRequest) {
        return adminClient.addUser(userId, newUserRequest);
    }

    @DeleteMapping("/{removableUserId}")
    public ResponseEntity<Object> deleteUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long removableUserId) {
        return adminClient.deleteUserById(userId, removableUserId);
    }

}
