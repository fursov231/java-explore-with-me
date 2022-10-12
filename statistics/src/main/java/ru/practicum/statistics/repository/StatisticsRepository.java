package ru.practicum.statistics.repository;

import com.vladmihalcea.hibernate.type.basic.Inet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statistics.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Stat, Long> {
    List<Stat> findAllByTimestampBetweenAndUri(LocalDateTime start, LocalDateTime end, String uri);


    @Query(value = "select distinct on (ip) ip, id, uri, app, timestamp " +
            "from stats " +
            "where timestamp > cast(:start as date) " +
            "and timestamp  < cast(:end as date) " +
            "and uri ilike :uri ", nativeQuery = true)
    List<Stat> findAllByTimestampBetweenAndUriUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select count (s) from Stat s where s.uri like :uri")
    Long findViews(String uri);
}
