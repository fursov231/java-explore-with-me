package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                        @PathVariable long userId) {
        return requestService.getAllRequests(ownerId, userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                              @PathVariable long userId,
                                              @RequestParam long eventId) {
        return requestService.addRequest(ownerId, userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @PathVariable long userId,
                                                @PathVariable long requestId) {
         requestService.cancelRequest(ownerId, userId, requestId);
         return ResponseEntity.ok("Запрос на участие удален");
    }
}
