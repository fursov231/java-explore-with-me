package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> findByInitiatorAndId(User initiator, long eventId);

    List<Event> findAllByInitiator_IdAndStateAndCategory_IdAndEventDateBetween(long userId, EventState state,
                                                                                    long categoryId, LocalDateTime rangeStart,
                                                                                    LocalDateTime rangeEnd, Pageable pageable);
    List<Event> findAllByCategory_Id(long catId);
}
