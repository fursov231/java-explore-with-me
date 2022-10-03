package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationById(long compId);

    CompilationDto addCompilation(long ownerId, NewCompilationDto newCompilationDto);

    void deleteCompilationById(long ownerId, long compId);

    void deleteCompilationByIdAndEventId(long ownerId, long compId, long eventId);

    void addEventInCompilation(long ownerId, long compId, long eventId);

    void unpinCompilationByIdOnMainPage(long ownerId, long compId);

    void pinCompilationByIdOnMainPage(long ownerId, long compId);
}
