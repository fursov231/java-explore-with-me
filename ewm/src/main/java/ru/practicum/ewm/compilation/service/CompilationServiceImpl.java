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

    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {

        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Compilation> compilationPage = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<Compilation> compilationList = compilationPage.getContent();

        List<CompilationDto> result = new ArrayList<>();

        for (var compilation : compilationList) {
            List<EventShortDto> events = new ArrayList<>();
            Optional<Compilation> optionalCompilation = compilationRepository.findById(compilation.getId());
            if (optionalCompilation.isPresent()) {
                List<Event> eventsList = optionalCompilation.get().getEvents();
                if (!eventsList.isEmpty()) {
                    eventsList.forEach(e -> events.add(EventMapper.toShortDto(e)));
                    result.add(CompilationDto.builder().id(compilation.getId()).title(compilation.getTitle()).pinned(compilation.isPinned())
                            .events(events).build());
                } else {
                    result.add(CompilationDto.builder().events(Collections.EMPTY_LIST).id(compilation.getId())
                            .title(compilation.getTitle()).pinned(compilation.isPinned()).build());
                }
            }

        }
        return result;
    }


    public CompilationDto getCompilationById(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isPresent()) {
            List<Event> events = compilationOptional.get().getEvents();
            List<EventShortDto> shortDtos = new ArrayList<>();
            if (!events.isEmpty()) {
                shortDtos = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
            }

            return CompilationDto.builder()
                    .id(compilationOptional.get().getId())
                    .title(compilationOptional.get().getTitle())
                    .pinned(compilationOptional.get().isPinned())
                    .events(shortDtos)
                    .build();
        }
        throw new NotFoundException("Запрошенной подборки не найдено");
    }

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Long> eventsId = newCompilationDto.getEvents();
        List<Event> events = new ArrayList<>();
        eventsId.forEach(e -> events.add(eventRepository.findById(e).get()));
        Compilation compilation = CompilationMapper.newCompDtoToComp(newCompilationDto);
        compilation.setEvents(events);
        Compilation savedCompilatation = compilationRepository.save(compilation);
        CompilationDto result = CompilationMapper.toDto(savedCompilatation);
        List<EventShortDto> eventDtos = new ArrayList<>();
        events.forEach(e -> eventDtos.add(EventMapper.toShortDto(e)));
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
            List<Event> events = compilationOptional.get().getEvents();
            events.add(eventOptional.get());
            compilationOptional.get().setEvents(events);
            compilationRepository.save(compilationOptional.get());
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
