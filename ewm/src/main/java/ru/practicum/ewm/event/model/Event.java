package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    private String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "location_id")
    private Long locationId;
    private boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    private String title;

    @JoinColumn(name = "initiator_id")
    @ManyToOne
    private User initiator;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    private String state;
    private Long views;
    private LocalDateTime created;

    @Column(name = "is_available")
    private boolean isAvailable;
}
