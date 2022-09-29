package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    private List<Integer> events;
    private boolean pinned;
    private String title;
}
