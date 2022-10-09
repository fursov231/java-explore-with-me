package ru.practicum.ewm.compilation.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JdbCompilationsEventsDao {
    private final JdbcTemplate jdbcTemplate;

    public void saveCompilation(long compId, long eventId) {
        String sql = "insert into compilations_events (compilation_id, event_id) " +
                "values (?, ?);";
        jdbcTemplate.update(sql, compId, eventId);
    }

    public List<Long> findEventIdByCompilationId(long compilationId) {
        String sql = String.format("select ce.event_id from compilations_events ce " +
                "where ce.compilation_id = %d", compilationId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("event_id"));
    }
}
