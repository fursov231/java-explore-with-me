package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.client.RequestPrivateClient;

@RestController
@RequiredArgsConstructor
public class PrivateRequestController {
    private final RequestPrivateClient privateClient;

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long ownerId,  @PathVariable long userId) {
        return privateClient.getAllRequests(ownerId, userId);
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,  @PathVariable long userId, @RequestParam long eventId) {
        return privateClient.addRequest(ownerId, userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,  @PathVariable long userId, @PathVariable long requestId) {
        return privateClient.cancelRequest(ownerId, userId, requestId);
    }
}
