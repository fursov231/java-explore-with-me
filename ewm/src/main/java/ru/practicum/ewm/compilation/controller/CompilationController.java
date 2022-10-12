package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilations(@RequestParam boolean pinned,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        return compilationService.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilationById(@PathVariable long compId) {
        compilationService.deleteCompilationById(compId);
        return ResponseEntity.ok("Подборка удалена");
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<String> deleteCompilationByIdAndEventId(@PathVariable long compId,
                                                                  @PathVariable long eventId) {
        compilationService.deleteCompilationByIdAndEventId(compId, eventId);
        return ResponseEntity.ok("Событие удалено из подборки");
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<String> addCompilationByIdAndEventId(@PathVariable long compId,
                                                               @PathVariable long eventId) {
        compilationService.addEventInCompilation(compId, eventId);
        return ResponseEntity.ok("Событие добавлено в подборку");
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<String> unpinCompilationByIdOnMainPage(@PathVariable long compId) {
        compilationService.unpinCompilationByIdOnMainPage(compId);
        return ResponseEntity.ok("Подборка откреплена с главной страницы");
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<String> pinCompilationByIdOnMainPage(@PathVariable long compId) {
        compilationService.pinCompilationByIdOnMainPage(compId);
        return ResponseEntity.ok("Подборка закреплена на главной страницы");
    }
}
