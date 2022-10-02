package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestState;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllById(long id);

    void deleteByRequesterAndId(User user, Long id);

    List<Request> findRequestsByEvent_IdAndStatus(Long eventId, RequestState requestState);

    List<Request> findRequestsByEvent_IdAndRequester_Id(Long eventId, Long requesterId);
}
