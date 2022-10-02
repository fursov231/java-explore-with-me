package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEvents(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @RequestParam List<Long> users,
                                             @RequestParam List<String> states,
                                             @RequestParam List<Long> categories,
                                             @RequestParam LocalDateTime rangeStart,
                                             @RequestParam LocalDateTime rangeEnd,
                                             @RequestParam(name = "from") int from,
                                             @RequestParam(name = "size") int size) {
        return eventService.findEventsByAdmin(ownerId, users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public NewEventDto editEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                     @PathVariable long eventId,
                                     @RequestBody NewEventDto newEventDto) {
        return eventService.updateEventByAdmin(ownerId, eventId, newEventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                         @PathVariable long eventId) {
        return eventService.publishEventByAdmin(ownerId, eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                        @PathVariable long eventId) {
        return eventService.rejectEventByAdmin(ownerId, eventId);
    }
}
