package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateRequestController {
    private final RequestServiceImpl requestServiceImpl;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                        @PathVariable long userId) {
        return requestServiceImpl.getAllRequests(ownerId, userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                              @PathVariable long userId,
                                              @RequestParam long eventId) {
        return requestServiceImpl.addRequest(ownerId, userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @PathVariable long userId,
                                                @PathVariable long requestId) {
         requestServiceImpl.cancelRequest(ownerId, userId, requestId);
         return ResponseEntity.ok("Запрос на участие удален");
    }
}
