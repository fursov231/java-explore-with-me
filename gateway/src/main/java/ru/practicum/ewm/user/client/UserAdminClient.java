package ru.practicum.ewm.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.user.dto.NewUserRequest;

import java.util.Map;

@Service
public class UserAdminClient extends BaseClient {
    private static final String API_ADMIN_PREFIX = "/admin/users";

    @Autowired
    public UserAdminClient(@Value("${ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_ADMIN_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllUsers(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addUser(long userId, NewUserRequest newUserRequest) {
        return post("", userId, newUserRequest);
    }

    public ResponseEntity<Object> deleteUserById(long userId, long removableUserId) {
        return delete("/" + removableUserId, userId);
    }
}
