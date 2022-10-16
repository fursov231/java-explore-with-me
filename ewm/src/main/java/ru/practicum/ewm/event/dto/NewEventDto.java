package ru.practicum.ewm.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    @Size(max = 1000)
    private String annotation;

    private Long category;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Future
    private LocalDateTime eventDate;

    private LocationDto location;
    private boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;

    @NotBlank
    @Size(max = 128)
    private String title;
}


