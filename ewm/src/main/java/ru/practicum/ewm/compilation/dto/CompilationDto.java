package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    Long id;
    boolean pinned;
    String title;
}
