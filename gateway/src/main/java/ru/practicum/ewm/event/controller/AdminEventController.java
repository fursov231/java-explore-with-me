package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.client.EventAdminClient;
import ru.practicum.ewm.event.dto.NewEventDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventAdminClient adminClient;

    @GetMapping("/admin/events")
    public ResponseEntity<Object> findEvents(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @RequestParam List<Integer> users,
                                             @RequestParam List<String> states,
                                             @RequestParam List<Integer> categories,
                                             @RequestParam LocalDateTime rangeStart,
                                             @RequestParam LocalDateTime rangeEnd,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminClient.findEventsByAdmin(ownerId, users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public ResponseEntity<Object> editEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @PathVariable Integer eventId,
                                                @RequestBody NewEventDto newEventDto) {
        return adminClient.updateEventByAdmin(ownerId, eventId, newEventDto);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<Object> publishEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                   @PathVariable Integer eventId) {
        return adminClient.publishEventByAdmin(ownerId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<Object> rejectEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @PathVariable Integer eventId) {
        return adminClient.rejectEventByAdmin(ownerId, eventId);
    }

}
