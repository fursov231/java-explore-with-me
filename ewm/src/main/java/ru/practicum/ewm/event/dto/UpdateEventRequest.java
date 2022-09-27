package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UpdateEventRequest {
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Integer eventId;
    private boolean paid;
    private Integer participantLimit;
    private String title;
}
