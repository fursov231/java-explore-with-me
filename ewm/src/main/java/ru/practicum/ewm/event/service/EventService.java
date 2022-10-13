package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, boolean onlyAvailable, SortValue sort, int from,
                                     int size, HttpServletRequest request);

    EventFullDto getEventById(long id, HttpServletRequest request);

    List<EventShortDto> getAllUsersEvents(long userId, int from, int size, HttpServletRequest request);

    EventFullDto updateEvent(long userId, UpdateEventRequest updateEventRequest, HttpServletRequest request);

    EventFullDto addEvent(long userId, NewEventDto newEventDto, HttpServletRequest request);

    EventFullDto getUsersEventById(long userId, long eventId, HttpServletRequest request);

    EventFullDto cancelEvent(long userId, long eventId, HttpServletRequest request);

    List<ParticipationRequestDto> getRequests(long userId, long eventId);

    ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId);

    ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId);

    List<EventFullDto> findEventsByAdmin(List<Long> users, List<EventState> states,
                                         List<Long> categories, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, int from, int size, HttpServletRequest request);

    EventFullDto updateEventByAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest, HttpServletRequest request);

    EventFullDto publishEventByAdmin(long eventId, HttpServletRequest request);

    EventFullDto rejectEventByAdmin(long eventId, HttpServletRequest request);

    CommentResponseDto addNewComment(long userId, long eventId, CommentRequestDto commentRequestDto);

    CommentResponseDto updateComment(long userId, long eventId, UpdateCommentDto updateCommentDto);

    void deleteComment(long userId, long eventId, long commentId);
}
