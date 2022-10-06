package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilationById(@PathVariable long compId) {
        compilationService.deleteCompilationById(compId);
        return ResponseEntity.ok("Подборка удалена");
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<String> deleteCompilationByIdAndEventId(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.deleteCompilationByIdAndEventId(compId, eventId);
        return ResponseEntity.ok("Событие удалено из подборки");
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<String> addCompilationByIdAndEventId(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.addEventInCompilation(compId, eventId);
        return ResponseEntity.ok("Событие добавлено в подборку");
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<String> unpinCompilationByIdOnMainPage(@PathVariable long compId) {
        compilationService.unpinCompilationByIdOnMainPage(compId);
        return ResponseEntity.ok("Подборка откреплена с главной страницы");
    }

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<String> pinCompilationByIdOnMainPage(@PathVariable long compId) {
        compilationService.pinCompilationByIdOnMainPage(compId);
        return ResponseEntity.ok("Подборка закреплена на главной страницы");
    }
}
