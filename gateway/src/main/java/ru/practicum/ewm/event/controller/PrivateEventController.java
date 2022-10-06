package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.client.EventPrivateClient;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final EventPrivateClient privateClient;

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<Object> getAllUsersEvents(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                    @PathVariable long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return privateClient.getAllUsersEvents(ownerId, userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public ResponseEntity<Object> updateUsersEvent(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                   @PathVariable long userId, UpdateEventRequest updateEventRequest) {
        return privateClient.updateEvent(ownerId, userId, updateEventRequest);
    }

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<Object> addUsersEvent(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @PathVariable long userId, NewEventDto newEventDto) {
        return privateClient.addEvent(ownerId, userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<Object> getUsersEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                    @PathVariable long userId, @PathVariable Integer eventId) {
        return privateClient.getEventById(ownerId, userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<Object> cancelUsersEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                       @PathVariable long userId, @PathVariable Integer eventId) {
        return privateClient.cancelEvent(ownerId, userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                              @PathVariable long userId, @PathVariable Integer eventId) {
        return privateClient.getRequests(ownerId, userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/{reqId}/confirm")
    public ResponseEntity<Object> confirmRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                 @PathVariable long userId, @PathVariable Integer eventId,
                                                 @PathVariable Integer reqId) {
        return privateClient.confirmRequest(ownerId, userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/{reqId}/reject")
    public ResponseEntity<Object> rejectRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @PathVariable long userId, @PathVariable Integer eventId,
                                                @PathVariable Integer reqId) {
        return privateClient.rejectRequest(ownerId, userId, eventId, reqId);
    }
}
