package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.client.EventPublicClient;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventPublicClient publicClient;

    @GetMapping("/events")
    public ResponseEntity<Object> getAllEvents(@RequestParam(name = "text") String text,
                                               @RequestParam(name = "categories") List<Integer> categories,
                                               @RequestParam(name = "paid") boolean paid,
                                               @RequestParam(name = "rangeStart") LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd") LocalDateTime rangeEnd,
                                               @RequestParam(name = "onlyAvailable") boolean onlyAvailable,
                                               @RequestParam(name = "sort") String sort,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicClient.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable long id) {
        return publicClient.getEventById(id);
    }
}
