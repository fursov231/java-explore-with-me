package ru.practicum.ewm.category.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.client.BaseClient;

@Service
public class CategoryAdminClient extends BaseClient {
    private static final String API_ADMIN_PREFIX = "/admin/categories";

    @Autowired
    public CategoryAdminClient(@Value("$ {ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_ADMIN_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> updateCategory(long userId, CategoryDto categoryDto) {
        return patch("", userId, categoryDto);
    }

    public ResponseEntity<Object> addCategory(long userId, CategoryRequestDto categoryRequestDto) {
        return post("", userId, categoryRequestDto);
    }

    public ResponseEntity<Object> deleteCategory(long userId, long catId) {
        return delete("/" + catId, userId);
    }
}
