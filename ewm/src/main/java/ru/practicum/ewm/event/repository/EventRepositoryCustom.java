package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByParams(String text, Category category, boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                                     String sort, Pageable pageable);
}
