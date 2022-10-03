package ru.practicum.ewm.event.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.SortValue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    private final EventRepository eventRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EventRepositoryCustomImpl(@Lazy EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }



    @Override
    @SuppressWarnings("unchecked")
    public List<Event> findByParams(String text, Category category, boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, boolean onlyAvailable, SortValue sort, Pageable pageable) {
        return entityManager.createNativeQuery(
                "select e.id, e.annotation " +
                "from events e " +
                        "join requests r on e.id = r.event_id " +
                        "where e.description ilike :text " +
                        "or e.annotation ilike :text " +
                        "and e.category_id = :category " +
                        "and e.paid = :paid " +
                        "and e.event_date > (case when :rangeStart is null then now() else :rangeStart end) " +
                        "and e.event_date < (case when :rangeEnd is null then now() else :rangeEnd end) " +
                        "and e.state = 'PUBLISHED' " +
                        "and r.status = 'CONFIRMED' " +
                        "and e.is_available = :onlyAvailable " +
                        "order by :sort", Event.class)
                .setParameter("text", text)
                .setParameter("category", category)
                .setParameter("paid", paid)
                .setParameter("rangeStart", rangeStart)
                .setParameter("rangeEnd", rangeEnd)
                .setParameter("onlyAvailable", onlyAvailable)
                .setParameter("sort", sort.name())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }


}
