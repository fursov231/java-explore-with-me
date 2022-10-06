package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.compilation.client.CompilationPublicClient;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationPublicClient publicClient;

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
}
