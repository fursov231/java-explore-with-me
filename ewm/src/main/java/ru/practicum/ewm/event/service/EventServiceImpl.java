package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.CommentRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.event.util.CommentMapper;
import ru.practicum.ewm.event.util.EventMapper;
import ru.practicum.ewm.event.util.LocationMapper;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;

    @Value("${stats.url}")
    private String statsUrl;

    WebClient client = WebClient.create();

    public List<EventShortDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, boolean onlyAvailable, SortValue sort, int from, int size) {
        List<Event> events = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(from / size, size);

        for (Integer categoryId : categories) {
            Optional<Category> category = categoryRepository.findById(categoryId.longValue());
            category.ifPresent(value -> events.addAll(eventRepository.findByParams(text, value, paid, rangeStart,
                    rangeEnd, onlyAvailable, String.valueOf(sort), pageRequest)));
        }
        List<EventShortDto> shortDtos = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
        shortDtos.forEach(e -> e.setConfirmedRequests(
                requestRepository.findRequestsByEvent_idAndStatus(e.getId(), String.valueOf(RequestState.CONFIRMED)).size())
        );
        shortDtos.forEach(e -> e.setViews(getViews(e.getId())));
        return shortDtos;
    }

    public EventFullDto getEventById(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("?????????????????? EventId ???? ????????????"));
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        if (event.getState().equals(EventState.PUBLISHED)) {
            eventFullDto.setConfirmedRequests(
                    requestRepository.findRequestsByEvent_idAndStatus(eventFullDto.getId(), String.valueOf(RequestState.CONFIRMED)).size()
            );
            Optional<Location> location = locationRepository.findById(event.getLocationId());
            location.ifPresent(e -> eventFullDto.setLocation(LocationMapper.toDto(e)));
            eventFullDto.setViews(getViews(eventFullDto.getId()));
            eventFullDto.setComment(findComments(eventFullDto.getId()));
            sendHitRequest(request);
        }
        return eventFullDto;
    }

    public List<EventShortDto> getAllUsersEvents(long userId, int from, int size) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            List<Event> events = eventRepository.findAllByInitiator(optionalTargetUser.get(), pageRequest);
            List<EventShortDto> shortDtos = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
            shortDtos.forEach(e -> e.setConfirmedRequests(requestRepository.findRequestsByEvent_idAndStatus(e.getId(),
                    String.valueOf(RequestState.CONFIRMED)).size()));
            shortDtos.forEach(e -> e.setViews(getViews(e.getId())));
            return shortDtos;
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    @Transactional
    public EventFullDto updateEvent(long userId, UpdateEventRequest updateEventRequest) {
        findUser(userId);
        Event event = findEvent(updateEventRequest.getEventId());
        if (LocalDateTime.now().isBefore(updateEventRequest.getEventDate().minusHours(2))) {
            if (event.getState().equals(EventState.PENDING)
                    || !event.getState().equals((EventState.CANCELED))) {
                Event updatedEvent = eventUpdater(event, updateEventRequest);
                eventRepository.save(updatedEvent);
                EventFullDto result = EventMapper.toFullDto(updatedEvent);
                Optional<Location> location = locationRepository.findById(event.getLocationId());
                location.ifPresent(value -> result.setLocation(LocationMapper.toDto(value)));
                result.setConfirmedRequests(requestRepository.findRequestsByEvent_idAndStatus(result.getId(),
                        String.valueOf(RequestState.CONFIRMED)).size());
                result.setViews(getViews(event.getId()));
                result.setComment(findComments(event.getId()));
                log.info("?????????????? id=${} ??????????????????", updateEventRequest.getEventId());
                return result;
            } else {
                throw new ForbiddenException("???????????????????? ?????????????? event ????????????????????");
            }
        } else {
            throw new ValidationException("?????????? ?????????????? ???? ?????????? ???????? ???????????? ?????? ???? 2 ???????? ???? ???????????????? ??????????????");
        }
    }

    @Transactional
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        if (LocalDateTime.now().isBefore(newEventDto.getEventDate().minusHours(2))) {
            Optional<User> optionalTargetUser = userRepository.findById(userId);
            Optional<Category> optionalCategory = categoryRepository.findById(newEventDto.getCategory());
            if (optionalTargetUser.isPresent()) {
                Event event = EventMapper.newDtoToEvent(newEventDto);
                Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
                event.setLocationId(location.getId());
                event.setInitiator(optionalTargetUser.get());
                optionalCategory.ifPresent(event::setCategory);
                Event savedEvent = eventRepository.save(event);
                EventFullDto result = EventMapper.toFullDto(savedEvent);
                result.setLocation(LocationMapper.toDto(location));
                result.setConfirmedRequests(0);
                result.setViews(0L);
                result.setComment(new ArrayList<>());
                log.info("?????????????? id=${} ??????????????????", result.getId());
                return result;
            } else {
                throw new NotFoundException("?????????????????? userId ???? ????????????");
            }
        } else {
            throw new ValidationException("?????????? ?????????????? ???? ?????????? ???????? ???????????? ?????? ???? 2 ???????? ???? ???????????????? ??????????????");
        }
    }

    public EventFullDto getUsersEventById(long userId, long eventId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findByInitiatorAndId(optionalTargetUser.get(), eventId);
            if (event.isPresent()) {
                EventFullDto result = EventMapper.toFullDto(event.get());
                result.setConfirmedRequests(requestRepository.findRequestsByEvent_idAndStatus(result.getId(),
                        String.valueOf(RequestState.CONFIRMED)).size());
                Optional<Location> location = locationRepository.findById(event.get().getLocationId());
                location.ifPresent(e -> result.setLocation(LocationMapper.toDto(e)));
                result.setViews(getViews(eventId));
                result.setComment(findComments(eventId));
                return result;
            } else {
                throw new NotFoundException("?????????????????? event ???? ????????????");
            }
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    @Transactional
    public EventFullDto cancelEvent(long userId, long eventId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findByInitiatorAndId(optionalTargetUser.get(), eventId);
            if (event.isPresent()) {
                if (event.get().getInitiator().getId() == userId) {
                    if (event.get().getState().equals(EventState.PENDING)) {
                        event.get().setState(EventState.CANCELED);
                        eventRepository.save(event.get());
                        EventFullDto removableEvent = EventMapper.toFullDto(event.get());
                        removableEvent
                                .setConfirmedRequests(requestRepository.findRequestsByEvent_idAndStatus(removableEvent.getId(),
                                        String.valueOf(RequestState.CONFIRMED)).size());
                        Optional<Location> location = locationRepository.findById(event.get().getLocationId());
                        location.ifPresent(e -> removableEvent.setLocation(LocationMapper.toDto(e)));
                        removableEvent.setViews(getViews(eventId));
                        removableEvent.setComment(findComments(eventId));
                        log.info("?????????????? id=${} ??????????????????", removableEvent.getId());
                        return removableEvent;
                    } else {
                        throw new ValidationException("?????????????? ???????????? ???????? ?? ???????????????? ?????????????????? ");
                    }
                } else {
                    throw new ValidationException("???????????? ???? ???????????????????? userId ????????????????");
                }
            } else {
                throw new NotFoundException("?????????????????? event ???? ????????????");
            }
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                List<Request> requests = requestRepository.findRequestsByEvent_Id(eventId);
                if (event.get().getInitiator().getId() == userId) {
                    return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
                }
                return Collections.emptyList();
            } else {
                throw new NotFoundException("?????????????????? eventId ???? ????????????");
            }
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    @Transactional
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            Optional<Request> optionalRequest = requestRepository.findById(reqId);
            if (optionalEvent.isPresent() && optionalRequest.isPresent()) {
                if (optionalEvent.get().getParticipantLimit() == 0 || !optionalEvent.get().isRequestModeration()) {
                    return RequestMapper.toDto(optionalRequest.get());
                }
                long currentNumOfRequests = requestRepository.findRequestsByEvent_idAndStatus(eventId,
                        String.valueOf(RequestState.CONFIRMED)).size();
                if (currentNumOfRequests >= optionalEvent.get().getParticipantLimit()) {
                    throw new ValidationException("?????????????????? ?????????? ????????????");
                }
                optionalRequest.get().setStatus(String.valueOf(RequestState.CONFIRMED));
                long newCurrentNumOfRequests = requestRepository.findRequestsByEvent_idAndStatus(eventId,
                        String.valueOf(RequestState.CONFIRMED)).size();
                if (optionalEvent.get().getParticipantLimit() == newCurrentNumOfRequests) {
                    optionalEvent.get().setAvailable(true);
                    eventRepository.save(optionalEvent.get());
                }
                Request request = requestRepository.save(optionalRequest.get());
                log.info("???????????? ???? ?????????????? id=${} ??????????????????????", request.getId());
                return RequestMapper.toDto(request);
            } else {
                throw new NotFoundException("???? ?????????????? ???? ???????????????????? eventId ?????? requesterId");
            }
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    @Transactional
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            Optional<Request> optionalRequest = requestRepository.findById(reqId);
            if (optionalEvent.isPresent() && optionalRequest.isPresent()) {
                optionalRequest.get().setStatus(String.valueOf(RequestState.REJECTED));
                Request request = requestRepository.save(optionalRequest.get());
                long newCurrentNumOfRequests = requestRepository.findRequestsByEvent_idAndStatus(eventId,
                        String.valueOf(RequestState.CONFIRMED)).size();
                if (optionalEvent.get().getParticipantLimit() < newCurrentNumOfRequests) {
                    optionalEvent.get().setAvailable(false);
                    eventRepository.save(optionalEvent.get());
                }
                log.info("???????????? ???? ?????????????? id=${} ????????????????", request.getId());
                return RequestMapper.toDto(request);
            } else {
                throw new NotFoundException("???? ?????????????? ???? ???????????????????? eventId ?????? requesterId");
            }
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    public List<EventFullDto> findEventsByAdmin(List<Long> users, List<EventState> states,
                                                List<Long> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<Event> events = new ArrayList<>();
        List<EventFullDto> result = new ArrayList<>();
        if (!users.isEmpty() && !states.isEmpty() && !categories.isEmpty()) {
            for (Long user : users) {
                for (EventState state : states) {
                    for (Long category : categories) {
                        events = eventRepository
                                .findAllByInitiator_IdAndStateAndCategory_IdAndEventDateBetween(
                                        user, state, category, rangeStart,
                                        rangeEnd, pageRequest);
                    }
                }
            }
            for (var event : events) {
                EventFullDto eventFullDto = EventMapper.toFullDto(event);
                eventFullDto.setConfirmedRequests(requestRepository.findRequestsByEvent_idAndStatus(event.getId(),
                        String.valueOf(RequestState.CONFIRMED)).size());
                Optional<Location> location = locationRepository.findById(event.getLocationId());
                location.ifPresent(value -> eventFullDto.setLocation(LocationMapper.toDto(value)));
                eventFullDto.setViews(getViews(event.getId()));
                eventFullDto.setComment(findComments(event.getId()));
                result.add(eventFullDto);
            }
        }
        return result;
    }

    @Transactional
    public EventFullDto updateEventByAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Optional<Event> targetEvent = eventRepository.findById(eventId);
        if (targetEvent.isPresent()) {
            Event event = EventMapper.adminDtoToEvent(adminUpdateEventRequest);
            event.setLocationId(targetEvent.get().getLocationId());
            event.setCategory(targetEvent.get().getCategory());
            event.setInitiator(targetEvent.get().getInitiator());
            event.setPublishedOn(targetEvent.get().getPublishedOn());
            event.setAvailable(targetEvent.get().isAvailable());
            event.setCreated(targetEvent.get().getCreated());
            event.setState(targetEvent.get().getState());
            EventFullDto result = EventMapper.toFullDto(eventRepository.save(event));
            long currentNumOfRequests = requestRepository.findRequestsByEvent_idAndStatus(eventId,
                    String.valueOf(RequestState.CONFIRMED)).size();
            result.setConfirmedRequests((int) currentNumOfRequests);
            Optional<Location> location = locationRepository.findById(targetEvent.get().getLocationId());
            location.ifPresent(value -> result.setLocation(LocationMapper.toDto(value)));
            Long views = getViews(eventId);
            result.setViews(views);
            result.setComment(findComments(eventId));
            log.info("?????????????? id=${} ?????????????????? ??????????????????????????????", event.getId());
            return result;
        } else {
            throw new NotFoundException("Event ???? ????????????");
        }
    }

    @Transactional
    public EventFullDto publishEventByAdmin(long eventId) {
        Optional<Event> targetEvent = eventRepository.findById(eventId);
        if (targetEvent.isPresent()) {
            if (LocalDateTime.now().isBefore(targetEvent.get().getEventDate().minusHours(1))) {
                if (targetEvent.get().getState().equals(EventState.PENDING)) {
                    targetEvent.get().setPublishedOn(LocalDateTime.now());
                    targetEvent.get().setState(EventState.PUBLISHED);
                    EventFullDto eventFullDto = EventMapper.toFullDto(eventRepository.save(targetEvent.get()));
                    Optional<Location> location = locationRepository.findById(targetEvent.get().getLocationId());
                    location.ifPresent(value -> eventFullDto.setLocation(LocationMapper.toDto(value)));
                    eventFullDto.setViews(getViews(eventId));
                    eventFullDto.setComment(findComments(eventId));
                    log.info("?????????????? id=${} ???????????????????????? ??????????????????????????????", eventFullDto.getId());
                    return eventFullDto;
                } else {
                    throw new ValidationException("?????????????? ???????????? ???????? ?? ?????????????????? ???????????????? ????????????????????");
                }
            } else {
                throw new ValidationException("???????? ???????????? ?????????????? ???????????? ???????? ???? ?????????? ?????? ???? ?????? ???? ???????? ????????????????????");
            }

        } else {
            throw new NotFoundException("Event ???? ????????????");
        }
    }

    @Transactional
    public EventFullDto rejectEventByAdmin(long eventId) {
        Optional<Event> targetEvent = eventRepository.findById(eventId);
        if (targetEvent.isPresent()) {
            if (targetEvent.get().getState().equals(EventState.PENDING)) {
                targetEvent.get().setState(EventState.CANCELED);
                EventFullDto eventFullDto = EventMapper.toFullDto(eventRepository.save(targetEvent.get()));
                Optional<Location> location = locationRepository.findById(targetEvent.get().getLocationId());
                location.ifPresent(value -> eventFullDto.setLocation(LocationMapper.toDto(value)));
                eventFullDto.setViews(eventId);
                eventFullDto.setComment(findComments(eventId));
                log.info("?????????????? id=${} ?????????????????? ??????????????????????????????", eventFullDto.getId());
                return eventFullDto;
            } else {
                throw new ValidationException("?????????????? ???????????? ???????? ?? ?????????????????? ???????????????? ????????????????????");
            }
        } else {
            throw new NotFoundException("Event ???? ????????????");
        }
    }

    @Transactional
    @Override
    public CommentResponseDto addNewComment(long userId, long eventId, CommentRequestDto commentRequestDto) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalEvent.isPresent() && optionalUser.isPresent()) {
            if (optionalEvent.get().getState().equals(EventState.PUBLISHED)) {
                Comment comment = CommentMapper.toEvent(commentRequestDto);
                comment.setEvent(optionalEvent.get());
                comment.setAuthor(optionalUser.get());
                Comment savedComment = commentRepository.save(comment);
                CommentResponseDto commentResponseDto = CommentMapper.toResponseDto(savedComment);
                commentResponseDto.getEvent().setConfirmedRequests(
                        requestRepository.findRequestsByEvent_idAndStatus(eventId,
                                String.valueOf(RequestState.CONFIRMED)).size());
                commentResponseDto.getEvent().setViews(getViews(eventId));
                log.info("?????????????????????? id=${} ????????????????", savedComment.getId());
                return commentResponseDto;
            } else {
                throw new ValidationException("???????????????????? ???????????????? ?????????????????????? ???? ???????????????????????????????? ?????? ???????????????????? ??????????????");
            }
        } else {
            throw new NotFoundException("Event ?????? User ???? ????????????");
        }
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(long userId, long eventId, long commendId, UpdateCommentDto updateCommentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(commendId);
        if (optionalComment.isPresent()) {
            Comment comment = CommentMapper.toEvent(updateCommentDto);
            comment.setId(commendId);
            comment.setAuthor(optionalComment.get().getAuthor());
            comment.setEvent(optionalComment.get().getEvent());
            Comment savedComment = commentRepository.save(comment);
            CommentResponseDto commentResponseDto = CommentMapper.toResponseDto(savedComment);
            commentResponseDto.getEvent().setConfirmedRequests(
                    requestRepository.findRequestsByEvent_idAndStatus(eventId,
                            String.valueOf(RequestState.CONFIRMED)).size());
            commentResponseDto.getEvent().setViews(getViews(eventId));
            log.info("?????????????????????? id=${} ????????????????", comment.getId());
            return commentResponseDto;
        } else {
            throw new NotFoundException("?????????????????? ?????????????????????? ???? ????????????");
        }
    }

    @Transactional
    @Override
    public void deleteComment(long userId, long eventId, long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalEvent.isPresent() && optionalUser.isPresent()
                    && optionalEvent.get().getId() == eventId && optionalUser.get().getId() == userId) {
                commentRepository.deleteById(commentId);
                log.info("?????????????????????? id=${} ????????????", commentId);
            } else {
                throw new ValidationException("???????????? ???????????????? userId ?????? eventId");
            }
        } else {
            throw new NotFoundException("?????????????????? commentId ???? ????????????");
        }
    }

    private Event eventUpdater(Event event, UpdateEventRequest updateEventRequest) {
        event.setAnnotation(updateEventRequest.getAnnotation());
        event.setCategory(categoryRepository.findById(updateEventRequest.getCategory()).get());
        event.setDescription(updateEventRequest.getDescription());
        event.setEventDate(updateEventRequest.getEventDate());
        event.setId(updateEventRequest.getEventId());
        event.setPaid(updateEventRequest.isPaid());
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        event.setTitle(updateEventRequest.getTitle());
        if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }
        return event;
    }

    private void sendHitRequest(HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app("ewm-service")
                .uri(String.valueOf(request.getRequestURI()))
                .ip(String.valueOf(request.getRemoteAddr()))
                .timestamp(LocalDateTime.now())
                .build();

        client.post()
                .uri(statsUrl + "/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(endpointHit), EndpointHit.class)
                .exchange()
                .block();
    }

    private Long getViews(Long id) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(statsUrl + "/views")
                        .queryParam("uri", "/events/" + id)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    private User findUser(long userId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            return optionalTargetUser.get();
        } else {
            throw new NotFoundException("?????????????????? userId ???? ????????????");
        }
    }

    private Event findEvent(long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else {
            throw new NotFoundException("???????????????? eventId ???? ????????????");
        }
    }

    private List<CommentShortDto> findComments(long eventId) {
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId);
        return comments
                .stream()
                .map(CommentMapper::toShortDto)
                .collect(Collectors.toList());
    }


}
