package ru.practicum.statistics.model;

import com.vladmihalcea.hibernate.type.basic.Inet;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLInetType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stats", schema = "public")
@Builder
@TypeDef(
        name = "ipv4",
        typeClass = PostgreSQLInetType.class,
        defaultForType = Inet.class
)
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;
    private String uri;
    private Inet ip;
    private LocalDateTime timestamp;
}
