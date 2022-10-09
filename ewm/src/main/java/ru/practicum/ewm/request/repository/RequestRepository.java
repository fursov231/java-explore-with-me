package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    void deleteByRequesterAndId(User user, Long id);

    @Query(value = "select r.* from requests r where r.event_id = :eventId and r.status = :requestState", nativeQuery = true)
    List<Request> findRequestsByEvent_idAndStatus(Long eventId, String requestState);

    List<Request> findRequestsByEvent_Id(Long eventId);

    Optional<Request> findRequestByEvent_IdAndRequester_Id(Long eventId, Long requesterId);

    List<Request> findAllByRequester_id(Long userId);
}
