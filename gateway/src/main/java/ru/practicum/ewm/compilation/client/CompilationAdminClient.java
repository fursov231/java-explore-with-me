package ru.practicum.ewm.compilation.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

@Service
public class CompilationAdminClient extends BaseClient {
    private static final String API_ADMIN_PREFIX = "/admin/compilations";

    @Autowired
    public CompilationAdminClient(@Value("$ {ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_ADMIN_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addCompilation(long ownerId, NewCompilationDto newCompilationDto) {

        return post("", ownerId, newCompilationDto);
    }

    public ResponseEntity<Object> deleteCompilationById(long ownerId, long compId) {
        return delete("/" + compId, ownerId);
    }

    public ResponseEntity<Object> deleteCompilationByIdAndEventId(long ownerId, long compId, long eventId) {
        return delete("/" + compId + "/events/" + eventId, ownerId);
    }

    public ResponseEntity<Object> addEventInCompilation(long ownerId, long compId, long eventId) {
        return patch("/" + compId + "/events/" + eventId, ownerId);
    }

    public ResponseEntity<Object> unpinCompilationByIdOnMainPage(long ownerId, long compId) {
        return delete("/" + compId + "/pin", ownerId);
    }

    public ResponseEntity<Object> pinCompilationByIdOnMainPage(long ownerId, long compId) {
        return patch("/" + compId + "/pin", ownerId);
    }
}

