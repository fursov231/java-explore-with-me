package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class AdminUpdateEventRequest {
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Point location;
    private boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;
    private String title;
}
