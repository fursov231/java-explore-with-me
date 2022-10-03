package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestState;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.util.RequestMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> getAllRequests(long ownerId, long userId) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (optionalUser.isPresent()) {
            List<Request> requests = requestRepository.findAllById(userId);
            for (var request : requests) {
                Optional<Event> event = eventRepository.findById(request.getEvent().getId());
                Optional<User> user = userRepository.findById(request.getRequester().getId());
                if (event.isPresent() && user.isPresent()) {
                    result.add(RequestMapper.toDto(request));
                }
            }
            return result;
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Transactional
    public ParticipationRequestDto addRequest(long ownerId, long userId, long eventId) {
        Optional<User> optionalUser = userRepository.findById(ownerId);
        Optional<Request> currentRequest = requestRepository.findRequestByEvent_IdAndRequester_Id(eventId, userId);
        if (currentRequest.isPresent()) {
            throw new ValidationException("Нельзя добавить повторный запрос");
        }
        if (userId == ownerId) {
            throw new ValidationException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (optionalUser.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            Optional<Event> event = eventRepository.findById(eventId);
            if (user.isPresent() && event.isPresent()) {
                if (event.get().getState().equals(EventState.PUBLISHED)) {
                    long currentNumOfRequests = requestRepository.findRequestsByEvent_IdAndStatus(eventId,
                            RequestState.CONFIRMED).size();
                    if (currentNumOfRequests >= event.get().getParticipantLimit()) {
                        throw new ForbiddenException("Достигнут лимит запросов на участие");
                    }
                    Request request = Request.builder().created(LocalDateTime.now()).requester(user.get()).event(event.get())
                            .status(RequestState.PENDING).build();
                    if (!event.get().isRequestModeration()) {
                        request.setStatus(RequestState.CONFIRMED);
                    }
                    return RequestMapper.toDto(requestRepository.save(request));
                } else {
                    throw new ForbiddenException("Нельзя участвовать в неопубликованном событии");
                }
            }
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }

    @Transactional
    public void cancelRequest(long ownerId, long userId, long requestId) {
        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (optionalUser.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(value -> requestRepository.deleteByRequesterAndId(value, requestId));
        }
        throw new NotFoundException("Указанный пользователь не найден");
    }
}
