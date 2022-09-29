package ru.practicum.ewm.compilation.util;

import ru.practicum.ewm.category.util.CategoryMapper;
import ru.practicum.ewm.compilation.dto.EventShortDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.model.Event;

public class CompilationMapper {


    public static Compilation newCompDtoToComp(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
