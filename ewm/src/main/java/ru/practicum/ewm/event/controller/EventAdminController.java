package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEvents(@RequestParam List<Long> users,
                                         @RequestParam List<EventState> states,
                                         @RequestParam List<Long> categories,
                                         @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}")
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto editEventById(@PathVariable long eventId,
                                      @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventService.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEventById(@PathVariable long eventId) {
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEventById(@PathVariable long eventId) {
        return eventService.rejectEventByAdmin(eventId);
    }
}
