package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories") List<Integer> categories,
                                            @RequestParam(name = "paid") boolean paid,
                                            @RequestParam(name = "rangeStart") String rangeStart,
                                            @RequestParam(name = "rangeEnd") String rangeEnd,
                                            @RequestParam(name = "onlyAvailable") boolean onlyAvailable,
                                            @RequestParam(name = "sort") String sort,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                            HttpServletRequest request)  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventService.getAllEvents(text, categories, paid, start, end, onlyAvailable, SortValue.valueOf(sort), from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable long id, HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }
}
