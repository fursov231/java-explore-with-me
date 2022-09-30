package ru.practicum.ewm.compilation.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;


public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "select ce.event_id from compilations_events ce " +
            "where ce.compilation_id = :compilationId", nativeQuery = true)
    List<Long> findEventIdByCompilationId(long compilationId);

    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

    @Modifying
    @Query(value = "insert into compilations_events (compilation_id, event_id) VALUES(:compId, :eventId)",
            nativeQuery = true)
    void saveCompilation(long compId, long eventId);


}
