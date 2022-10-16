package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventFullDto extends EventShortDto {
    private LocalDateTime createdOn;
    private String description;
    private LocationDto location;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private EventState state;
    private List<CommentShortDto> comment;

    @Builder
    public EventFullDto(String annotation, CategoryDto category, LocalDateTime eventDate, Long id, UserShortDto initiator,
                        boolean paid, String title, Integer confirmedRequests, Long views, LocalDateTime createdOn,
                        String description, LocationDto location, Integer participantLimit, LocalDateTime publishedOn,
                        boolean requestModeration, EventState state, List<CommentShortDto> comment) {
        super(annotation, category, eventDate, id, initiator, paid, title, confirmedRequests, views);
        this.createdOn = createdOn;
        this.description = description;
        this.location = location;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.comment = comment;
    }
}