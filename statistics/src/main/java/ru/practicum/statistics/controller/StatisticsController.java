package ru.practicum.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.dto.EndpointHit;
import ru.practicum.statistics.dto.ViewStats;
import ru.practicum.statistics.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public ResponseEntity<String> save(@RequestBody EndpointHit endpointHit) {
        statisticsService.save(endpointHit);
        return ResponseEntity.ok("Информация сохранена");
    }

    @GetMapping("/stats")
    public List<ViewStats> findAllByParams(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam List <String> uris,
                                           @RequestParam boolean unique) {
        return statisticsService.findAllByParams(start, end, uris, unique);
    }
}
