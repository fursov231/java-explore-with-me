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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventAdminClient extends BaseClient {
    private static final String API_ADMIN_PREFIX = "/admin/events";

    @Autowired
    public EventAdminClient(@Value("${ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_ADMIN_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllEvents(long ownerId, List<Integer> users, List<String> states,
                                               List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "users", users,
                "states", states,
                "categories", categories,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size
        );
        return get("?users={users}&states={states}&categories={categories}&rangeStart={rangeStart}&rangeEnd={rangeEnd}&" +
                "from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> updateEvent(long ownerId, Integer eventId, NewEventDto newEventDto) {
        return put("/" + eventId, ownerId, newEventDto);
    }

    public ResponseEntity<Object> publishEvent(long ownerId, Integer eventId) {
        return patch("/" + eventId + "/publish", ownerId);
    }

    public ResponseEntity<Object> rejectEvent(long ownerId, Integer eventId) {
        return patch("/" + eventId + "/reject", ownerId);
    }
}
