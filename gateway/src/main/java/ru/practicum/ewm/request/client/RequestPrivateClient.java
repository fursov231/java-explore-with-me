package ru.practicum.ewm.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;

import java.util.Map;

@Service
public class RequestPrivateClient extends BaseClient {
    private static final String API_PRIVATE_PREFIX = "/users";

    @Autowired
    public RequestPrivateClient(@Value("$ {ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PRIVATE_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllRequests(long ownerId, long userId) {
        return get("/" + userId + "/requests", ownerId);
    }

    public ResponseEntity<Object> addRequest(long ownerId, long userId, long eventId) {
        Map<String, Object> parameters = Map.of(
                "eventId", eventId);
        return post("/" + userId + "/requests", ownerId, parameters);
    }

    public ResponseEntity<Object> cancelRequest(long ownerId, long userId, long requestId) {
        return post("/" + userId + "/requests/" + requestId + "/cancel", ownerId);
    }
}
