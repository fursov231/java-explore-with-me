package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventService eventService;

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
}
