package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventFullDto {

    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime eventDate;
    private Integer id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;

    private LocalDateTime createdOn;
    private String description;
    private Location location;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private EventState eventState;


}