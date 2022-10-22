package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllUsersEvents(@PathVariable long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.getAllUsersEvents(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateUsersEvent(@PathVariable long userId,
                                               @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping
    public EventFullDto addUsersEvent(@PathVariable long userId,
                                     @RequestBody NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUsersEventById(@PathVariable long userId,
                                          @PathVariable long eventId) {
        return eventService.getUsersEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelUsersEventById(@PathVariable long userId,
                                             @PathVariable long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                                     @PathVariable long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @PathVariable long reqId) {
        return eventService.rejectRequest(userId, eventId, reqId);
    }

    @PostMapping("/{eventId}/comments")
    public CommentResponseDto addNewComment(@PathVariable long userId,
                                            @PathVariable long eventId, @RequestBody CommentRequestDto commentRequestDto) {
        return eventService.addNewComment(userId, eventId, commentRequestDto);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    public CommentResponseDto patchComment(@PathVariable long userId,
                                           @PathVariable long eventId,
                                           @PathVariable long commentId,
                                           @RequestBody UpdateCommentDto updateCommentDto) {
        return eventService.updateComment(userId, eventId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long userId,
                                        @PathVariable long eventId,
                                        @PathVariable long commentId) {
         eventService.deleteComment(userId, eventId, commentId);
         return ResponseEntity.ok("Комментарий удален");
    }
}
