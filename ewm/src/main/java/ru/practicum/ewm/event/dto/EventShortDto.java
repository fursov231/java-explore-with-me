package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto  {
    protected String annotation;
    protected CategoryDto category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
    protected Long id;
    protected UserShortDto initiator;
    protected boolean paid;
    protected String title;
    protected Integer confirmedRequests;
    protected Long views;
}
