package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationServiceImpl compilationServiceImpl;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam boolean pinned,
                                                   @RequestParam(name = "from") int from,
                                                   @RequestParam(name = "size") int size) {
        return compilationServiceImpl.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        return compilationServiceImpl.getCompilationById(compId);
    }
}
