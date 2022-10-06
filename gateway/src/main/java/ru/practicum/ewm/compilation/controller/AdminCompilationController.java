package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.client.CompilationAdminClient;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {
    private final CompilationAdminClient adminClient;

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        return adminClient.deleteCompilationByIdAndEventId(ownerId, compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<Object> addCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        return adminClient.addEventInCompilation(ownerId, compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<Object> unpinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        return adminClient.unpinCompilationByIdOnMainPage(ownerId, compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<Object> pinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        return adminClient.pinCompilationByIdOnMainPage(ownerId, compId);
    }
}
