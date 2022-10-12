package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationById(long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilationById(long compId);

    void deleteCompilationByIdAndEventId(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void unpinCompilationByIdOnMainPage(long compId);

    void pinCompilationByIdOnMainPage(long compId);
}
