package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class AdminUpdateEventRequest {
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private Integer paticipantLimit;
    private boolean requestModeration;
    private String title;
}
