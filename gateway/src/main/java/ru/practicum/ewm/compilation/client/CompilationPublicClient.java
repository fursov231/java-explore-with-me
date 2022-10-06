package ru.practicum.ewm.compilation.client;

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
public class CompilationPublicClient extends BaseClient {
    private static final String API_PUBLIC_PREFIX = "/compilations";

    @Autowired
    public CompilationPublicClient(@Value("$ {ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PUBLIC_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllCompilations(boolean pinned, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "pinned", pinned,
                "from", from,
                "size", size
        );
        return get("?pinned={pinned}&from={from}&size={size}", parameters);
    }

    public ResponseEntity<Object> getCompilationById(long compId) {
        return get("/" + compId);
    }
}

