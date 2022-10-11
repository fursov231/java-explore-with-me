package ru.practicum.ewm.event.util;

import ru.practicum.ewm.category.util.CategoryMapper;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.util.UserMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EventMapper {
    //+ set confirmedRequest, views
    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                event.getEventDate(), event.getId(), UserMapper.toUserShortDto(event.getInitiator()), event.isPaid(),
                event.getTitle());
    }

    //+ set confirmedRequest, locationDto, views
    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .createdOn(event.getCreated())
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .build();
    }

    //+ set categoryId, locationDto, initiator, views, created
    public static Event toEvent(UpdateEventRequest updateEventRequest) {
        return Event.builder()
                .id(updateEventRequest.getEventId())
                .annotation(updateEventRequest.getAnnotation())
                .description(updateEventRequest.getDescription())
                .eventDate(updateEventRequest.getEventDate().truncatedTo(ChronoUnit.SECONDS))
                .paid(updateEventRequest.isPaid())
                .participantLimit(updateEventRequest.getParticipantLimit())
                .title(updateEventRequest.getTitle())
                .build();
    }

    //+locationDto
    public static Event fullDtoToEvent(EventFullDto eventFullDto) {
        return Event.builder()
                .id(eventFullDto.getId())
                .category(CategoryMapper.toCategory(eventFullDto.getCategory()))
                .annotation(eventFullDto.getAnnotation())
                .description(eventFullDto.getDescription())
                .eventDate(eventFullDto.getEventDate().truncatedTo(ChronoUnit.SECONDS))
                .paid(eventFullDto.isPaid())
                .participantLimit(eventFullDto.getParticipantLimit())
                .title(eventFullDto.getTitle())
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

    //+ set category, initiator, publishedOn, + locationId
    public static Event newDtoToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate().truncatedTo(ChronoUnit.SECONDS))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .state(EventState.PENDING)
                .isAvailable(false)
                .created(LocalDateTime.now())
                .build();
    }

    //+ set category, initiator, publishedOn, available, views, created, state, locationDto
    public static Event adminDtoToEvent(AdminUpdateEventRequest adminUpdateEventRequest) {
        return Event.builder()
                .annotation(adminUpdateEventRequest.getAnnotation())
                .description(adminUpdateEventRequest.getDescription())
                .eventDate(adminUpdateEventRequest.getEventDate().truncatedTo(ChronoUnit.SECONDS))
                .paid(adminUpdateEventRequest.isPaid())
                .participantLimit(adminUpdateEventRequest.getParticipantLimit())
                .requestModeration(adminUpdateEventRequest.isRequestModeration())
                .title(adminUpdateEventRequest.getTitle())
                .build();
    }
    //+ locationDto
    public static NewEventDto toNewDtoFromEvent(Event event) {
        return NewEventDto.builder()
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .category(event.getCategory().getId())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .build();
    }
}

