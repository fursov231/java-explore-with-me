package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.client.CompilationAdminClient;
import ru.practicum.ewm.compilation.client.CompilationPublicClient;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationPublicClient publicClient;
    private final CompilationAdminClient adminClient;

    @GetMapping("/compilations")
    public ResponseEntity<Object> getAllCompilations(@RequestParam boolean pinned,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicClient.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable long compId) {
        return publicClient.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    public ResponseEntity<Object> addCompilation(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody NewCompilationDto newCompilationDto) {
        return adminClient.addCompilation(ownerId, newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilationById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        return adminClient.deleteCompilationById(ownerId, compId);
    }

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
