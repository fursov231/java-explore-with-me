package ru.practicum.ewm.event.model;

import lombok.*;
import org.springframework.data.geo.Point;
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

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private Point location;
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

    private State state;
}
