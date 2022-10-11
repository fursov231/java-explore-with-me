package ru.practicum.ewm.compilation.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;


public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

     List<Long> findCompilationsEvents (long compilationId);
    }

