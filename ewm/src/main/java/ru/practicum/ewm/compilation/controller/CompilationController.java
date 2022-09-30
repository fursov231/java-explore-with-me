package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
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
        compilationService.deleteCompilationById(ownerId, compId);
        return ResponseEntity.ok("Подборка удалена");
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<String> deleteCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        compilationService.deleteCompilationByIdAndEventId(ownerId, compId, eventId);
        return ResponseEntity.ok("Событие удалено из подборки");
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<String> addCompilationByIdAndEventId(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId, @PathVariable long eventId) {
        compilationService.addEventInCompilation(ownerId, compId, eventId);
        return ResponseEntity.ok("Событие добавлено в подборку");
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<String> unpinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        compilationService.unpinCompilationByIdOnMainPage(ownerId, compId);
        return ResponseEntity.ok("Подборка откреплена с главной страницы");
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<String> pinCompilationByIdOnMainPage(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long compId) {
        compilationService.pinCompilationByIdOnMainPage(ownerId, compId);
        return ResponseEntity.ok("Подборка закреплена на главной страницы");
    }
}
