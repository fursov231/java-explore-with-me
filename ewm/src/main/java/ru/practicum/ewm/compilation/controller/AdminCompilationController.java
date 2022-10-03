package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationServiceImpl compilationServiceImpl;

    @PostMapping
    public CompilationDto addCompilation(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody NewCompilationDto newCompilationDto) {
        return compilationServiceImpl.addCompilation(ownerId, newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilationById(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        compilationServiceImpl.deleteCompilationById(ownerId, compId);
        return ResponseEntity.ok("Подборка удалена");
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<String> deleteCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        compilationServiceImpl.deleteCompilationByIdAndEventId(ownerId, compId, eventId);
        return ResponseEntity.ok("Событие удалено из подборки");
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<String> addCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        compilationServiceImpl.addEventInCompilation(ownerId, compId, eventId);
        return ResponseEntity.ok("Событие добавлено в подборку");
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<String> unpinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        compilationServiceImpl.unpinCompilationByIdOnMainPage(ownerId, compId);
        return ResponseEntity.ok("Подборка откреплена с главной страницы");
    }

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<String> pinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        compilationServiceImpl.pinCompilationByIdOnMainPage(ownerId, compId);
        return ResponseEntity.ok("Подборка закреплена на главной страницы");
    }
}
