package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
    protected Long id;
    protected UserShortDto initiator;
    protected boolean paid;
    protected String title;
    protected Long views;
    private LocalDateTime createdOn;
    private String description;
    private LocationDto location;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private EventState state;
}