package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.SortValue;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    //todo сервис статистики
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


    //todo сервис статистики
    public EventFullDto getEventById(long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent() && event.get().getState().equals(EventState.PUBLISHED)) {
            EventFullDto eventFullDto = EventMapper.toFullDto(event.get());
            eventFullDto.setConfirmedRequests(
                    requestRepository.findRequestsByEvent_IdAndStatus(eventFullDto.getId(), RequestState.CONFIRMED).size()
            );
            Optional<Location> location = locationRepository.findById(event.get().getLocationId());
            location.ifPresent(e -> eventFullDto.setLocation(LocationMapper.toDto(e)));
            return eventFullDto;
        } else {
            throw new NotFoundException("Указанный EventId не найден");
        }
    }

    public List<EventShortDto> getAllUsersEvents(long userId, int from, int size) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            List<Event> events = eventRepository.findAllByInitiator(optionalTargetUser.get(), pageRequest);
            List<EventShortDto> shortDtos = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
            shortDtos.forEach(e -> e.setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(e.getId(),
                    RequestState.CONFIRMED).size()));
            return shortDtos;
        } else {
            throw new NotFoundException("Указанный userId не найден");
        }
    }

    @Transactional
    public UpdateEventRequest updateEvent(long userId, UpdateEventRequest updateEventRequest) {

        Optional<User> optionalTargetUser = userRepository.findById(userId);

        if (LocalDateTime.now().isBefore(updateEventRequest.getEventDate().minusHours(2))) {
            if (optionalTargetUser.isPresent()) {
                Optional<Event> targetEvent = eventRepository.findById(updateEventRequest.getEventId());
                if (targetEvent.isPresent()) {
                    if (!targetEvent.get().getState().equals(EventState.PENDING)
                            || !targetEvent.get().getState().equals(EventState.CANCELED)) {
                        Event newEvent = EventMapper.toEvent(updateEventRequest);
                        newEvent.setCategory(targetEvent.get().getCategory());
                        if (targetEvent.get().getState().equals(EventState.CANCELED)) {
                            newEvent.setState(EventState.PENDING);
                        }
                        newEvent.setAvailable(targetEvent.get().isAvailable());
                        Event updatedEvent = eventRepository.save(newEvent);
                        return EventMapper.toUpdatedDto(updatedEvent);
                    } else {
                        throw new ForbiddenException("Обновление данного event невозможно");
                    }
                } else {
                    throw new NotFoundException("Указанный Event не найден");
                }
            } else {
                throw new NotFoundException("Указанный userId не найден");
            }
        } else {
            throw new ValidationException("Время события не может быть раньше чем за 2 часа от текущего момента");
        }
    }

    @Transactional
    public NewEventDto addEvent(long userId, NewEventDto newEventDto) {
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
                NewEventDto result = EventMapper.toNewDtoFromEvent(savedEvent);
                result.setLocation(LocationMapper.toDto(location));
                return result;
            } else {
                throw new NotFoundException("Указанный userId не найден");
            }
        } else {
            throw new ValidationException("Время события не может быть раньше чем за 2 часа от текущего момента");
        }
    }

    public EventFullDto getUsersEventById(long userId, long eventId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findByInitiatorAndId(optionalTargetUser.get(), eventId);
            if (event.isPresent()) {
                EventFullDto result = EventMapper.toFullDto(event.get());
                result.setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(result.getId(),
                        RequestState.CONFIRMED).size());
                Optional<Location> location = locationRepository.findById(event.get().getLocationId());
                location.ifPresent(e -> result.setLocation(LocationMapper.toDto(e)));
                return result;
            } else {
                throw new NotFoundException("Указанный event не найден");
            }
        } else {
            throw new NotFoundException("Указанный userId не найден");
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
                        EventFullDto removableEvent = EventMapper.toFullDto(event.get());
                        removableEvent
                                .setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(removableEvent.getId(),
                                        RequestState.CONFIRMED).size());
                        Optional<Location> location = locationRepository.findById(event.get().getLocationId());
                        location.ifPresent(e -> removableEvent.setLocation(LocationMapper.toDto(e)));
                        eventRepository.deleteById(eventId);
                        return removableEvent;
                    } else {
                        throw new ValidationException("Событие должно быть в ожидании модерации ");
                    }
                } else {
                    throw new ValidationException("Доступ по указанному userId запрещен");
                }
            } else {
                throw new NotFoundException("Указанный event не найден");
            }
        } else {
            throw new NotFoundException("Указанный userId не найден");
        }
    }

    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                List<Request> requests = requestRepository.findRequestsByEvent_IdAndRequester_Id(eventId, userId);
                return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
            } else {
                throw new NotFoundException("Указанный eventId не найден");
            }
        } else {
            throw new NotFoundException("Указанный userId не найден");
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
                long currentNumOfRequests = requestRepository.findRequestsByEvent_IdAndStatus(eventId,
                        RequestState.CONFIRMED).size();
                if (optionalEvent.get().getParticipantLimit() >= currentNumOfRequests) {
                    throw new ValidationException("Достигнут лимит заявок");
                }
                optionalRequest.get().setStatus(RequestState.CONFIRMED);
                long newCurrentNumOfRequests = requestRepository.findRequestsByEvent_IdAndStatus(eventId,
                        RequestState.CONFIRMED).size();
                if (optionalEvent.get().getParticipantLimit() == newCurrentNumOfRequests) {
                    optionalEvent.get().setAvailable(false);
                    eventRepository.save(optionalEvent.get());
                }
                Request request = requestRepository.save(optionalRequest.get());
                return RequestMapper.toDto(request);
            } else {
                throw new NotFoundException("Не найдено по указанному eventId или requesterId");
            }
        } else {
            throw new NotFoundException("Указанный userId не найден");
        }
    }

    @Transactional
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Optional<User> optionalTargetUser = userRepository.findById(userId);
        if (optionalTargetUser.isPresent()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            Optional<Request> optionalRequest = requestRepository.findById(reqId);
            if (optionalEvent.isPresent() && optionalRequest.isPresent()) {
                optionalRequest.get().setStatus(RequestState.CANCELED);
                Request request = requestRepository.save(optionalRequest.get());
                long newCurrentNumOfRequests = requestRepository.findRequestsByEvent_IdAndStatus(eventId,
                        RequestState.CONFIRMED).size();
                if (optionalEvent.get().getParticipantLimit() < newCurrentNumOfRequests) {
                    optionalEvent.get().setAvailable(true);
                    eventRepository.save(optionalEvent.get());
                }
                return RequestMapper.toDto(request);
            } else {
                throw new NotFoundException("Не найдено по указанному eventId или requesterId");
            }
        } else {
            throw new NotFoundException("Указанный userId не найден");
        }
    }

    public List<EventFullDto> findEventsByAdmin(List<Long> users, List<String> states,
                                                List<Long> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<Event> events = new ArrayList<>();
        List<EventFullDto> result = new ArrayList<>();
        if (!users.isEmpty() && !states.isEmpty() && !categories.isEmpty()) {
            for (Long user : users) {
                for (String state : states) {
                    for (Long category : categories) {
                        events = eventRepository
                                .findAllByInitiator_IdAndStateAndCategory_IdAndEventDateBetween(
                                        user, EventState.valueOf(state), category, rangeStart,
                                        rangeEnd, pageRequest);
                    }
                }
            }
            for (var event : events) {
                EventFullDto eventFullDto = EventMapper.toFullDto(event);
                eventFullDto.setConfirmedRequests(requestRepository.findRequestsByEvent_IdAndStatus(event.getId(),
                        RequestState.CONFIRMED).size());
                Optional<Location> location = locationRepository.findById(event.getLocationId());
                location.ifPresent(value -> eventFullDto.setLocation(LocationMapper.toDto(value)));
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
            Location location = locationRepository.save(LocationMapper.toLocation(adminUpdateEventRequest.getLocation()));
            event.setLocationId(location.getId());
            event.setCategory(targetEvent.get().getCategory());
            event.setInitiator(targetEvent.get().getInitiator());
            event.setPublishedOn(targetEvent.get().getPublishedOn());
            event.setAvailable(targetEvent.get().isAvailable());
            event.setViews(targetEvent.get().getViews());
            event.setCreated(targetEvent.get().getCreated());
            event.setState(targetEvent.get().getState());
            EventFullDto result = EventMapper.toFullDto(eventRepository.save(event));
            long currentNumOfRequests = requestRepository.findRequestsByEvent_IdAndStatus(eventId,
                    RequestState.CONFIRMED).size();
            result.setConfirmedRequests((int) currentNumOfRequests);
            result.setLocation(LocationMapper.toDto(location));
            return result;
        } else {
            throw new NotFoundException("Event не найден");
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
                    return eventFullDto;
                } else {
                    throw new ValidationException("Событие должно быть в состоянии ожидания публикации");
                }
            } else {
                throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
            }

        } else {
            throw new NotFoundException("Event не найден");
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
                return eventFullDto;
            } else {
                throw new ValidationException("Событие должно быть в состоянии ожидания публикации");
            }
        } else {
            throw new NotFoundException("Event не найден");
        }
    }
}
