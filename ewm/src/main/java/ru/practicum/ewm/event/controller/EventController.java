package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories") List<Integer> categories,
                                            @RequestParam(name = "paid") boolean paid,
                                            @RequestParam(name = "rangeStart") String rangeStart,
                                            @RequestParam(name = "rangeEnd") String rangeEnd,
                                            @RequestParam(name = "onlyAvailable") boolean onlyAvailable,
                                            @RequestParam(name = "sort") String sort,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") int size)  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventService.getAllEvents(text, categories, paid, start, end, onlyAvailable, SortValue.valueOf(sort), from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAllUsersEvents(@PathVariable long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.getAllUsersEvents(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateUsersEvent(@PathVariable long userId,
                                               @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto addUsersEvent(@PathVariable long userId,
                                     @RequestBody NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUsersEventById(@PathVariable long userId,
                                          @PathVariable long eventId) {
        return eventService.getUsersEventById(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelUsersEventById(@PathVariable long userId,
                                             @PathVariable long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                                     @PathVariable long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @PathVariable long reqId) {
        return eventService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> findEvents(@RequestParam List<Long> users,
                                         @RequestParam List<String> states,
                                         @RequestParam List<Long> categories,
                                         @RequestParam String rangeStart,
                                         @RequestParam String rangeEnd,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventService.findEventsByAdmin(users, states, categories, start, end, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto editEventById(@PathVariable long eventId,
                                      @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventService.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEventById(@PathVariable long eventId) {
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEventById(@PathVariable long eventId) {
        return eventService.rejectEventByAdmin(eventId);
    }
}
