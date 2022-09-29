package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.EventShortDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.util.CompilationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventMapper;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        List<CompilationDto> result = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Compilation> compilationPage = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<Compilation> compilationList = compilationPage.getContent();

        for (var compilation : compilationList) {
            List<EventShortDto> events = new ArrayList<>();
            List<Long> eventsIdList = compilationRepository.findEventIdByCompilationId(compilation.getId());
            if (!eventsIdList.isEmpty()) {
                eventsIdList.forEach(e -> events.add(EventMapper.toShortDto(eventRepository.findById(e).get())));
                CompilationDto.builder().id(compilation.getId()).title(compilation.getTitle()).pinned(compilation.isPinned())
                        .events(events).build();
            }
        }
        return result;
    }


    public CompilationDto getCompilationById(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isPresent()) {
            List<Long> eventId = compilationRepository.findEventIdByCompilationId(compId);
            List<EventShortDto> events = eventId.stream().map(e -> EventMapper.toShortDto(eventRepository.findById(e)
                    .get())).collect(Collectors.toList());
                return CompilationDto.builder()
                        .id(compilationOptional.get().getId())
                        .title(compilationOptional.get().getTitle())
                        .pinned(compilationOptional.get().isPinned())
                        .events(events)
                        .build();
        }
        throw new NotFoundException("Запрошенной подборки не найдено");
    }

    public CompilationDto addCompilation(long ownerId, NewCompilationDto newCompilationDto) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isPresent()) {
            List<Integer> events = newCompilationDto.getEvents();
            Compilation compilation = compilationRepository.save(CompilationMapper.newCompDtoToComp(newCompilationDto));
            events.forEach(e -> compilationRepository.saveCompilation(compilation.getId(), e));
            return CompilationMapper.newCompDtoToComp(compilation);
        }
        throw new NotFoundException("Указанного пользователя не существует");
    }

    public ResponseEntity<Object> deleteCompilationById(long ownerId, long compId) {
    }

    public ResponseEntity<Object> deleteCompilationByIdAndEventId(long ownerId, long compId, long eventId) {
    }

    public ResponseEntity<Object> addEventInCompilation(long ownerId, long compId, long eventId) {
    }

    public ResponseEntity<Object> unpinCompilationByIdOnMainPage(long ownerId, long compId) {
    }

    public ResponseEntity<Object> pinCompilationByIdOnMainPage(long ownerId, long compId) {
    }
}
