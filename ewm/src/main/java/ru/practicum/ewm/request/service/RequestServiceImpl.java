package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> getAllRequests(long userId) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        List<Request> requests = requestRepository.findAllByRequester_id(userId);
        for (var request : requests) {
            Optional<Event> event = eventRepository.findById(request.getEvent().getId());
            Optional<User> user = userRepository.findById(request.getRequester().getId());
            if (event.isPresent() && user.isPresent()) {
                result.add(RequestMapper.toDto(request));
            }
        }
        return result;
    }

    @Transactional
    public ParticipationRequestDto addRequest(long userId, long eventId) {
        Optional<Request> currentRequest = requestRepository.findRequestByEvent_IdAndRequester_Id(eventId, userId);
        if (currentRequest.isPresent()) {
            throw new ValidationException("Нельзя добавить повторный запрос");
        }
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (user.isPresent() && event.isPresent()) {
            if (userId == event.get().getInitiator().getId()) {
                throw new ValidationException("Инициатор события не может добавить запрос на участие в своём событии");
            }
            if (event.get().getState().equals(EventState.PUBLISHED)) {
                long currentNumOfRequests = requestRepository.findRequestsByEvent_idAndStatus(eventId,
                        String.valueOf(RequestState.CONFIRMED)).size();
                if (currentNumOfRequests != 0 && currentNumOfRequests >= event.get().getParticipantLimit()) {
                    throw new ForbiddenException("Достигнут лимит запросов на участие");
                }
                Request request = Request.builder().created(LocalDateTime.now()).requester(user.get()).event(event.get())
                        .status(String.valueOf(RequestState.PENDING)).build();
                if (!event.get().isRequestModeration()) {
                    request.setStatus(String.valueOf(RequestState.CONFIRMED));
                }
                log.info("Запрос на участие id=${} добавлен", request.getId());
                return RequestMapper.toDto(requestRepository.save(request));
            } else {
                throw new ForbiddenException("Нельзя участвовать в неопубликованном событии");
            }
        }
        throw new NotFoundException("Указанного userId или eventId не найдено");
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Request> request = requestRepository.findById(requestId);
        if (user.isPresent() && request.isPresent()) {
            request.get().setStatus(String.valueOf(RequestState.CANCELED));
            requestRepository.save(request.get());
            log.info("Запрос на участие id=${} отклонен", request.get().getId());
            return RequestMapper.toDto(request.get());
        } else {
            throw new NotFoundException("Указанный userId или requestId не найден");
        }
    }
}
