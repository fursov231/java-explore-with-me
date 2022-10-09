package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.repository.jdbc.JdbCompilationsEventsDao;
import ru.practicum.ewm.compilation.util.CompilationMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventMapper;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final JdbCompilationsEventsDao jdbCompilationsEventsDao;

    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {

        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Compilation> compilationPage = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<Compilation> compilationList = compilationPage.getContent();

        List<CompilationDto> result = new ArrayList<>();

        for (var compilation : compilationList) {
            List<EventShortDto> events = new ArrayList<>();
            List<Long> eventsIdList = jdbCompilationsEventsDao.findEventIdByCompilationId(compilation.getId());
            if (!eventsIdList.isEmpty()) {
                eventsIdList.forEach(e -> events.add(EventMapper.toShortDto(eventRepository.findById(e).get())));
                result.add(CompilationDto.builder().id(compilation.getId()).title(compilation.getTitle()).pinned(compilation.isPinned())
                        .events(events).build());
            } else {
                 result.add(CompilationDto.builder().events(Collections.EMPTY_LIST).id(compilation.getId())
                         .title(compilation.getTitle()).pinned(compilation.isPinned()).build());
            }
        }
        return result;
    }


    public CompilationDto getCompilationById(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isPresent()) {
            List<Long> eventId = jdbCompilationsEventsDao.findEventIdByCompilationId(compId);
            List<EventShortDto> events = new ArrayList<>();
            if (!eventId.isEmpty()) {
                events = eventId.stream().map(e -> EventMapper.toShortDto(eventRepository.findById(e)
                        .get())).collect(Collectors.toList());
            }

            return CompilationDto.builder()
                    .id(compilationOptional.get().getId())
                    .title(compilationOptional.get().getTitle())
                    .pinned(compilationOptional.get().isPinned())
                    .events(events)
                    .build();
        }
        throw new NotFoundException("Запрошенной подборки не найдено");
    }

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(CompilationMapper.newCompDtoToComp(newCompilationDto));
        CompilationDto result = CompilationMapper.toDto(compilation);
        List<Long> events = newCompilationDto.getEvents();

        List<EventShortDto> eventDtos = new ArrayList<>();
        if (!events.isEmpty()) {
            events.forEach(e -> jdbCompilationsEventsDao.saveCompilation(compilation.getId(), e));
            events.forEach(e -> eventDtos.add(EventMapper.toShortDto(eventRepository.findById(e).get())));
        }
        result.setEvents(eventDtos);
        return result;
    }

    @Transactional
    public void deleteCompilationById(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public void deleteCompilationByIdAndEventId(long compId, long eventId) {
        compilationRepository.deleteById(compId);
        eventRepository.deleteById(eventId);
    }


    @Transactional
    public void addEventInCompilation(long compId, long eventId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent() && compilationOptional.isPresent()) {
            jdbCompilationsEventsDao.saveCompilation(compId, eventId);
        } else {
            throw new NotFoundException("Не найден указанный compilationId или eventId");
        }
    }

    @Transactional
    public void unpinCompilationByIdOnMainPage(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isPresent()) {
            Compilation targetCompilation = compilationOptional.get();
            targetCompilation.setPinned(false);
            compilationRepository.save(targetCompilation);
        } else {
            throw new NotFoundException("Не найден указанный compilationId");
        }
    }

    @Transactional
    public void pinCompilationByIdOnMainPage(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isPresent()) {
            Compilation targetCompilation = compilationOptional.get();
            targetCompilation.setPinned(true);
            compilationRepository.save(targetCompilation);
        } else {
            throw new NotFoundException("Не найден указанный compilationId");
        }
    }
}
