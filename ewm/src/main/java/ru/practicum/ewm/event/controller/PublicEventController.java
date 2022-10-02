package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories") List<Integer> categories,
                                            @RequestParam(name = "paid") boolean paid,
                                            @RequestParam(name = "rangeStart") LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd") LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable") boolean onlyAvailable,
                                            @RequestParam(name = "sort") String sort,
                                            @RequestParam(name = "from") int from,
                                            @RequestParam(name = "size") int size) {
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, SortValue.valueOf(sort), from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable long id) {
        return eventService.getEventById(id);
    }
}
