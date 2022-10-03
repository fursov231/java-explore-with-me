package ru.practicum.ewm.event.util;

import ru.practicum.ewm.category.util.CategoryMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.util.UserMapper;

import java.time.LocalDateTime;

public class EventMapper {
    //+ set confirmedRequest
    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    //+ set confirmedRequest
    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .createdOn(event.getCreated())
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .build();
    }

    //+ set categoryId
    public static Event toEvent(UpdateEventRequest updateEventRequest) {
        return Event.builder()
                .id(updateEventRequest.getEventId())
                .annotation(updateEventRequest.getAnnotation())
                .description(updateEventRequest.getDescription())
                .eventDate(updateEventRequest.getEventDate())
                .paid(updateEventRequest.isPaid())
                .participantLimit(updateEventRequest.getParticipantLimit())
                .title(updateEventRequest.getTitle())
                .build();
    }

    public static UpdateEventRequest toUpdatedDto(Event event) {
        return UpdateEventRequest.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory().getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .eventId(event.getId())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .build();
    }

    //+ set category, initiator, publishedOn
    public static Event newDtoToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .state(EventState.PENDING)
                .views(0L)
                .isAvailable(true)
                .created(LocalDateTime.now())
                .build();
    }

    public static NewEventDto toNewDtoFromEvent(Event event) {
        return NewEventDto.builder()
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .category(event.getCategory().getId())
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .build();
    }
}

