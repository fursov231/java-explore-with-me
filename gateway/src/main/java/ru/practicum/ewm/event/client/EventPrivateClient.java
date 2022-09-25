package ru.practicum.ewm.event.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;

import java.util.Map;

@Service
public class EventPrivateClient extends BaseClient {
    private static final String API_PRIVATE_PREFIX = "/users";

    @Autowired
    public EventPrivateClient(@Value("${ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PRIVATE_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllEvents(long ownerId, long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/" + userId + "/events?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> updateEvent(long ownerId, long userId, UpdateEventRequest updateEventRequest) {
        return patch("/" + userId + "/events", ownerId, updateEventRequest);
    }


    public ResponseEntity<Object> addEvent(long ownerId, long userId, NewEventDto newEventDto) {
        return post("/" + userId + "/events", ownerId, newEventDto);
    }

    public ResponseEntity<Object> getEventById(long ownerId, long userId, Integer eventId) {
        return get("/" + userId + "/events/" + eventId, ownerId);
    }

    public ResponseEntity<Object> cancelEvent(long ownerId, long userId, Integer eventId) {
        return patch("/" + userId + "/events/" + eventId, ownerId);
    }

    public ResponseEntity<Object> getRequests(long ownerId, long userId, Integer eventId) {
        return get("/" + userId + "/events/" + eventId + "/requests", ownerId);
    }

    public ResponseEntity<Object> confirmRequest(long ownerId, long userId, Integer eventId, Integer reqId) {
        return patch("/" + userId + "/events/" + eventId + "/requests" + reqId + "/confirm", ownerId);
    }

    public ResponseEntity<Object> rejectRequest(long ownerId, long userId, Integer eventId, Integer reqId) {
        return patch("/" + userId + "/events/" + eventId + "/requests" + reqId + "/confirm", ownerId);
    }
}
