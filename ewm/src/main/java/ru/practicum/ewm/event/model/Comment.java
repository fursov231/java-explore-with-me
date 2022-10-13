package ru.practicum.ewm.event.model;

import lombok.*;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;

    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;

    private LocalDateTime created;
}