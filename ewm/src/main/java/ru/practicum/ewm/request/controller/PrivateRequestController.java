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
    public List<ParticipationRequestDto> getAllRequests(@PathVariable long userId) {
        return requestService.getAllRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addRequest(@PathVariable long userId,
                                              @RequestParam long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable long userId,
                                                @PathVariable long requestId) {
         requestService.cancelRequest(userId, requestId);
         return ResponseEntity.ok("Запрос на участие удален");
    }
}
