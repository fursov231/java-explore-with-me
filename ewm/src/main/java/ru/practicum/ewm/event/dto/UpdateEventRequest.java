package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Long eventId;
    private boolean paid;
    private Integer participantLimit;
    private String title;
}
