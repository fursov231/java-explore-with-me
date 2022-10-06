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
@RequestMapping(path = "/users")
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllUsersEvents(@PathVariable long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.getAllUsersEvents(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public UpdateEventRequest updateUsersEvent(@PathVariable long userId,
                                               UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public NewEventDto addUsersEvent(@PathVariable long userId,
                                     NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUsersEventById(@PathVariable long userId,
                                          @PathVariable long eventId) {
        return eventService.getUsersEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelUsersEventById(@PathVariable long userId,
                                             @PathVariable long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                                     @PathVariable long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @PathVariable long reqId) {
        return eventService.rejectRequest(userId, eventId, reqId);
    }
}
