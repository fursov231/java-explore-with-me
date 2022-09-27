package ru.practicum.ewm.compilation.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
@Builder
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean pinned;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;
}