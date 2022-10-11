package ru.practicum.ewm.compilation.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import java.util.List;

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

    @JoinTable(name = "compilations_events")
    @OneToMany
    private List<Event> events;
}