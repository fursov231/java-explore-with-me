package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public List<EventShortDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, boolean onlyAvailable, SortValue sort, int from, int size) {
        List<Event> events = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(from / size, size);

        for (Integer categoryId : categories) {
            Optional<Category> category = categoryRepository.findById(categoryId.longValue());
            category.ifPresent(value -> events.addAll(eventRepository.findByParams(text, value, paid, rangeStart,
                    rangeEnd, onlyAvailable, sort, pageRequest)));
        }
        List<EventShortDto> shortDtos = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
        shortDtos.forEach(e -> e.setConfirmedRequests(
                requestRepository.findRequestsByEvent_IdAndStatus(e.getId(), RequestState.CONFIRMED).size())
        );
        return shortDtos;
    }

    public EventFullDto getEventById(long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            EventFullDto eventFullDto = EventMapper.toFullDto(event.get());
            eventFullDto.setConfirmedRequests(
                    requestRepository.findRequestsByEvent_IdAndStatus(eventFullDto.getId(), RequestState.CONFIRMED).size()
            );
            return eventFullDto;
        } else {
            throw new NotFoundException("Указанный EventId не найден");
        }
    }

    public List<EventShortDto> getAllUsersEvents(long ownerId, long userId, int from, int size) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            List<Event> events = eventRepository.findAllByInitiator(optionalTargetUser.get(), pageRequest);
            List<EventShortDto> shortDtos = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
            shortDtos.forEach(e -> e.setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(e.getId(),
                    RequestState.CONFIRMED).size()));
            return shortDtos;
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public UpdateEventRequest updateEvent(long ownerId, long userId, UpdateEventRequest updateEventRequest) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);

        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Optional<Event> targetEvent = eventRepository.findById(updateEventRequest.getEventId());
            if (targetEvent.isPresent()) {
                Event newEvent = EventMapper.toEvent(updateEventRequest);
                newEvent.setCategory(targetEvent.get().getCategory());
                Event updatedEvent = eventRepository.save(newEvent);
                return EventMapper.toUpdatedDto(updatedEvent);
            } else {
                throw new NotFoundException("Указанный Event не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public NewEventDto addEvent(long ownerId, long userId, NewEventDto newEventDto) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        Optional<Category> optionalCategory = categoryRepository.findById(newEventDto.getCategory());
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Event event = EventMapper.newDtoToEvent(newEventDto);
            event.setInitiator(optionalTargetUser.get());
            optionalCategory.ifPresent(event::setCategory);
            Event savedEvent = eventRepository.save(event);
            return EventMapper.toNewDtoFromEvent(savedEvent);
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public EventFullDto getUsersEventById(long ownerId, long userId, long eventId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findByInitiatorAndId(optionalTargetUser.get(), eventId);
            if (event.isPresent()) {
                EventFullDto result = EventMapper.toFullDto(event.get());
                result.setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(result.getId(),
                        RequestState.CONFIRMED).size());
                return result;
            } else {
                throw new NotFoundException("Указанный event не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public EventFullDto cancelEvent(long ownerId, long userId, long eventId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findByInitiatorAndId(optionalTargetUser.get(), eventId);
            if (event.isPresent()) {
                if (event.get().getInitiator().getId() == userId) {
                    if (event.get().getEventState().equals(EventState.PENDING)) {
                        EventFullDto removableEvent = EventMapper.toFullDto(event.get());
                        removableEvent
                                .setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(removableEvent.getId(),
                                        RequestState.CONFIRMED).size());
                        eventRepository.deleteById(eventId);
                        return removableEvent;
                    } else {
                        throw new NotFoundException("Событий со статусом PENDING не найдено");
                    }
                } else {
                    throw new ValidationException("Доступ по указанному userId запрещен");
                }
            } else {
                throw new NotFoundException("Указанный event не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public List<ParticipationRequestDto> getRequests(long ownerId, long userId, long eventId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                List<Request> requests = requestRepository.findRequestsByEvent_IdAndRequester_Id(eventId, userId);
                return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
            } else {
                throw new NotFoundException("Указанный eventId не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public ParticipationRequestDto confirmRequest(long ownerId, long userId, long eventId, long reqId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            Optional<Request> optionalRequest = requestRepository.findById(reqId);
            if (optionalEvent.isPresent() && optionalRequest.isPresent()) {
                if (optionalEvent.get().getParticipantLimit() == 0 || !optionalEvent.get().isRequestModeration()) {
                    return RequestMapper.toDto(optionalRequest.get());
                }
                Integer currentNumOfRequests = requestRepository.findRequestsByEvent_IdAndStatus(eventId,
                        RequestState.CONFIRMED).size();
                if (optionalEvent.get().getParticipantLimit() >= currentNumOfRequests) {
                    throw new ValidationException("Достигнут лимит заявок");
                }
                optionalRequest.get().setStatus(RequestState.CONFIRMED);
                Request request = requestRepository.save(optionalRequest.get());
                return RequestMapper.toDto(request);
            } else {
                throw new NotFoundException("Не найдено по указанному eventId или requesterId");
            }
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public ParticipationRequestDto rejectRequest(long ownerId, long userId, long eventId, long reqId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalOwner.isPresent() && optionalTargetUser.isPresent()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            Optional<Request> optionalRequest = requestRepository.findById(reqId);
            if (optionalEvent.isPresent() && optionalRequest.isPresent()) {
                optionalRequest.get().setStatus(RequestState.CANCELED);
                Request request = requestRepository.save(optionalRequest.get());
                return RequestMapper.toDto(request);
            } else {
                throw new NotFoundException("Не найдено по указанному eventId или requesterId");
            }
        } else {
            throw new NotFoundException("Указанный ownerId или userId не найден");
        }
    }

    public List<EventFullDto> findEventsByAdmin(long ownerId, List<Long> users, List<String> states,
                                                List<Long> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, int from, int size) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<EventFullDto> result = new ArrayList<>();
        if (optionalOwner.isPresent()) {
            List<Event> events = new ArrayList<>();
            if (!users.isEmpty() && !states.isEmpty() && !categories.isEmpty()) {
                for (Long user : users) {
                    for (String state : states) {
                        for (Long category : categories) {
                            events = eventRepository
                                    .findAllByInitiator_IdAndEventStateAndCategory_IdAndEventDateBetween(
                                            user, EventState.valueOf(state), category, rangeStart,
                                            rangeEnd, pageRequest);
                        }
                    }
                }
                result = events.stream().map(EventMapper::toFullDto).collect(Collectors.toList());
                result.forEach(e -> e.setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(e.getId(),
                        RequestState.CONFIRMED).size()));
            }
            return result;
        } else {
            throw new NotFoundException("Указанный ownerId не найден");
        }
    }

    public NewEventDto updateEventByAdmin(long ownerId, long eventId, NewEventDto newEventDto) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        if (optionalOwner.isPresent()) {
            Optional<Event> targetEvent = eventRepository.findById(eventId);
            if (targetEvent.isPresent()) {
                Event event = EventMapper.newDtoToEvent(newEventDto);
                event.setCategory(event.getCategory());
                event.setInitiator(event.getInitiator());
                return EventMapper.toNewDtoFromEvent(eventRepository.save(event));
            } else {
                throw new NotFoundException("Event не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId не найден");
        }

    }

    public EventFullDto publishEventByAdmin(long ownerId, long eventId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        if (optionalOwner.isPresent()) {
            Optional<Event> targetEvent = eventRepository.findById(eventId);
            if (targetEvent.isPresent()) {
                if (LocalDateTime.now().isBefore(targetEvent.get().getEventDate().minusHours(1))) {
                    if (targetEvent.get().getEventState().equals(EventState.PENDING)) {
                        targetEvent.get().setPublishedOn(LocalDateTime.now());
                        targetEvent.get().setEventState(EventState.PUBLISHED);
                        return EventMapper.toFullDto(eventRepository.save(targetEvent.get()));
                    } else {
                        throw new ValidationException("Событие должно быть в состоянии ожидания публикации");
                    }
                } else {
                    throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
                }

            } else {
                throw new NotFoundException("Event не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId не найден");
        }
    }

    public EventFullDto rejectEventByAdmin(long ownerId, long eventId) {
        Optional<User> optionalOwner = userRepository.findById(ownerId);
        if (optionalOwner.isPresent()) {
            Optional<Event> targetEvent = eventRepository.findById(eventId);
            if (targetEvent.isPresent()) {
                if (targetEvent.get().getEventState().equals(EventState.PENDING)) {
                    targetEvent.get().setEventState(EventState.CANCELED);
                    return EventMapper.toFullDto(eventRepository.save(targetEvent.get()));
                } else {
                    throw new ValidationException("Событие должно быть в состоянии ожидания публикации");
                }
            } else {
                throw new NotFoundException("Event не найден");
            }
        } else {
            throw new NotFoundException("Указанный ownerId не найден");
        }
    }
}
