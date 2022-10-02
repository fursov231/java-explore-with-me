package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllUsersEvents(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                 @PathVariable long userId,
                                                 @RequestParam(name = "from") int from,
                                                 @RequestParam(name = "size") int size) {
        return eventService.getAllUsersEvents(ownerId, userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public UpdateEventRequest updateUsersEvent(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                               @PathVariable long userId,
                                               UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(ownerId, userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public NewEventDto addUsersEvent(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                     @PathVariable long userId,
                                     NewEventDto newEventDto) {
        return eventService.addEvent(ownerId, userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUsersEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                          @PathVariable long userId,
                                          @PathVariable long eventId) {
        return eventService.getUsersEventById(ownerId, userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelUsersEventById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @PathVariable long userId,
                                             @PathVariable long eventId) {
        return eventService.cancelEvent(ownerId, userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                     @PathVariable long userId,
                                                     @PathVariable long eventId) {
        return eventService.getRequests(ownerId, userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        return eventService.confirmRequest(ownerId, userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                 @PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @PathVariable long reqId) {
        return eventService.rejectRequest(ownerId, userId, eventId, reqId);
    }
}
