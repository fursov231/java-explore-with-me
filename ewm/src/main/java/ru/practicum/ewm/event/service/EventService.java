package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, boolean onlyAvailable, SortValue sort, int from, int size);

    EventFullDto getEventById(long id);

    List<EventShortDto> getAllUsersEvents(long ownerId, long userId, int from, int size);

    UpdateEventRequest updateEvent(long ownerId, long userId, UpdateEventRequest updateEventRequest);

    NewEventDto addEvent(long ownerId, long userId, NewEventDto newEventDto);

    EventFullDto getUsersEventById(long ownerId, long userId, long eventId);

    EventFullDto cancelEvent(long ownerId, long userId, long eventId);

    List<ParticipationRequestDto> getRequests(long ownerId, long userId, long eventId);

    ParticipationRequestDto confirmRequest(long ownerId, long userId, long eventId, long reqId);

    ParticipationRequestDto rejectRequest(long ownerId, long userId, long eventId, long reqId);

    List<EventFullDto> findEventsByAdmin(long ownerId, List<Long> users, List<String> states,
                                         List<Long> categories, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(long ownerId, long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publishEventByAdmin(long ownerId, long eventId);

    EventFullDto rejectEventByAdmin(long ownerId, long eventId);
}
