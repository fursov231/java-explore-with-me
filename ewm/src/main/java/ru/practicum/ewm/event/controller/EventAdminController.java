package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping("/admin/events")
    public List<EventFullDto> findEvents(@RequestParam List<Long> users,
                                         @RequestParam List<EventState> states,
                                         @RequestParam List<Long> categories,
                                         @RequestParam String rangeStart,
                                         @RequestParam String rangeEnd,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                         HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventService.findEventsByAdmin(users, states, categories, start, end, from, size, request);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto editEventById(@PathVariable long eventId,
                                      @RequestBody AdminUpdateEventRequest adminUpdateEventRequest,
                                      HttpServletRequest request) {
        return eventService.updateEventByAdmin(eventId, adminUpdateEventRequest, request);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEventById(@PathVariable long eventId, HttpServletRequest request) {
        return eventService.publishEventByAdmin(eventId, request);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEventById(@PathVariable long eventId, HttpServletRequest request) {
        return eventService.rejectEventByAdmin(eventId, request);
    }
}
