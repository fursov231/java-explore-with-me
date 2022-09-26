package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.client.EventAdminClient;
import ru.practicum.ewm.event.client.EventPrivateClient;
import ru.practicum.ewm.event.client.EventPublicClient;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventAdminClient adminClient;
    private final EventPrivateClient privateClient;
    private final EventPublicClient publicClient;

    @GetMapping("/events")
    public ResponseEntity<Object> getAllEvents(@RequestParam(name = "text") String text,
                                               @RequestParam(name = "categories") List<Integer> categories,
                                               @RequestParam(name = "paid") boolean paid,
                                               @RequestParam(name = "rangeStart") LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd") LocalDateTime rangeEnd,
                                               @RequestParam(name = "onlyAvailable") boolean onlyAvailable,
                                               @RequestParam(name = "sort") String sort,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicClient.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable long id) {
        return publicClient.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<Object> getAllUsersEvents(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                    @PathVariable long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return privateClient.getAllEvents(ownerId, userId, from, size);
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

    @GetMapping("/admin/events")
    public ResponseEntity<Object> findEvents(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @RequestParam List<Integer> users,
                                             @RequestParam List<String> states,
                                             @RequestParam List<Integer> categories,
                                             @RequestParam LocalDateTime rangeStart,
                                             @RequestParam LocalDateTime rangeEnd,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminClient.getAllEvents(ownerId, users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public ResponseEntity<Object> editEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @PathVariable Integer eventId,
                                                @RequestBody NewEventDto newEventDto) {
        return adminClient.updateEvent(ownerId, eventId, newEventDto);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<Object> publishEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                   @PathVariable Integer eventId) {
        return adminClient.publishEvent(ownerId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<Object> rejectEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @PathVariable Integer eventId) {
        return adminClient.rejectEvent(ownerId, eventId);
    }

}
