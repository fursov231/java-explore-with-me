package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


    public ResponseEntity<Object> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
    }

    public ResponseEntity<Object> getEventById(long id) {
    }

    public ResponseEntity<Object> getAllUsersEvents(long ownerId, long userId, int from, int size) {
    }

    public ResponseEntity<Object> updateEvent(long ownerId, long userId, UpdateEventRequest updateEventRequest) {
    }

    public ResponseEntity<Object> addEvent(long ownerId, long userId, NewEventDto newEventDto) {
    }

    public ResponseEntity<Object> getUsersEventById(long ownerId, long userId, Integer eventId) {
    }

    public ResponseEntity<Object> cancelEvent(long ownerId, long userId, Integer eventId) {
    }

    public ResponseEntity<Object> getRequests(long ownerId, long userId, Integer eventId) {
    }

    public ResponseEntity<Object> confirmRequest(long ownerId, long userId, Integer eventId, Integer reqId) {
    }

    public ResponseEntity<Object> rejectRequest(long ownerId, long userId, Integer eventId, Integer reqId) {
    }

    public ResponseEntity<Object> findEventsByAdmin(long ownerId, List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
    }

    public ResponseEntity<Object> updateEventByAdmin(long ownerId, Integer eventId, NewEventDto newEventDto) {
    }

    public ResponseEntity<Object> publishEventByAdmin(long ownerId, Integer eventId) {
    }

    public ResponseEntity<Object> rejectEventByAdmin(long ownerId, Integer eventId) {
    }
}
