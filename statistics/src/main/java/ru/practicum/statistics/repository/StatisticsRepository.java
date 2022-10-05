package ru.practicum.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statistics.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Stat, Long> {
    List<Stat> findAllByTimestampBetweenAndUri(LocalDateTime start, LocalDateTime end, String uri);


    @Query("select s " +
            "from Stat s " +
            "where s.timestamp > :start " +
            "and s.timestamp  < :end " +
            "and s.uri like :uri " +
            "group by s.uri")
    List<Stat> findAllByTimestampBetweenAndUriUnique(LocalDateTime start, LocalDateTime end, String uri);
}
