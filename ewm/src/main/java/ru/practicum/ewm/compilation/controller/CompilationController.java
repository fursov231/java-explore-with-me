package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.EventShortDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilations(@RequestParam boolean pinned,
                                                  @RequestParam(name = "from") int from,
                                                  @RequestParam(name = "size") int size) {

        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        return compilationService.getCompilationById(compId);
    }

    @Transactional
    @PostMapping("/admin/compilations")
    public CompilationDto addCompilation(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(ownerId, newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilationById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        return compilationService.deleteCompilationById(ownerId, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        return compilationService.deleteCompilationByIdAndEventId(ownerId, compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<Object> addCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        return compilationService.addEventInCompilation(ownerId, compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<Object> unpinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        return compilationService.unpinCompilationByIdOnMainPage(ownerId, compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<Object> pinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        return compilationService.pinCompilationByIdOnMainPage(ownerId, compId);
    }
}
