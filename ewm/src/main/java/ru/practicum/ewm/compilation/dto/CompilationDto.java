package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    Long id;
    boolean pinned;
    @NotBlank
    @Size(max = 128)
    String title;
}
