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
public class AdminUserController {
    private final UserAdminClient adminClient;

    @GetMapping("/admin/users")
    public ResponseEntity<Object> getAllUsers(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminClient.getAllUsers(ownerId, from, size);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<Object> addUser(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                          NewUserRequest newUserRequest) {
        return adminClient.addUser(ownerId, newUserRequest);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<Object> deleteUser(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                          @PathVariable long userId) {
        return adminClient.deleteUserById(ownerId, userId);
    }

}
