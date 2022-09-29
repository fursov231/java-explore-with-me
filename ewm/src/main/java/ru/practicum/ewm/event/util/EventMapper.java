package ru.practicum.ewm.event.util;

import ru.practicum.ewm.category.util.CategoryMapper;
import ru.practicum.ewm.compilation.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

public class EventMapper {
    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests() //todo откуда?
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .paid(event.isPaid())
                .title(event.getTitle())
                .views() //todo из stats?
                .build();
    }
    }
}
