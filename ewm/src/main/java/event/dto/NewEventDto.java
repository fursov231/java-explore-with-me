package event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewEventDto {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private Location location;
    private boolean paid;
    Integer participantLimit;
    private boolean requestModeration;
    private String title;
    
}
