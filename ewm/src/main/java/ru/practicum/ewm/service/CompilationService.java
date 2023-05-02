package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.dto.CompilationDto;
import ru.practicum.ewm.entity.dto.NewCompilationDto;
import ru.practicum.ewm.entity.dto.UpdateCompilationRequest;
import ru.practicum.ewm.entity.mapper.CompilationMapper;
import ru.practicum.ewm.error.exeptions.NotFoundException;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatisticService statisticService;

    @Autowired
    public CompilationService(
            CompilationRepository compilationRepository,
            EventRepository eventRepository,
            StatisticService statisticService
    ) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.statisticService = statisticService;
    }

    public List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        PageRequest pageable = PageRequest.of(from, size);
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageable).toList();
        Set<Long> eventIds = new HashSet<>();
        compilations.forEach(c -> eventIds.addAll(c.getEvents().stream().map(Event::getId).collect(Collectors.toSet())));
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        return CompilationMapper.toDtos(compilations, viewsMap);
    }

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toObject(newCompilationDto, events);
        compilation = compilationRepository.save(compilation);
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        return CompilationMapper.toDto(compilation, viewsMap);
    }

    public CompilationDto getCompilationById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + compilationId + " was not found")
        );
        Set<Long> eventIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        return CompilationMapper.toDto(compilation, viewsMap);
    }

    public void deleteById(Long compilationId) {
        compilationRepository.deleteById(compilationId);
    }

    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + compilationId + " was not found")
        );
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getEvents() != null && request.getEvents().size() > 0) {
            Set<Event> events = eventRepository.findByIdIn(request.getEvents());
            compilation.setEvents(events);
        }
        compilation = compilationRepository.save(compilation);
        Set<Long> eventIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewsMap = statisticService.getEventsViews(eventIds);
        return CompilationMapper.toDto(compilation, viewsMap);
    }
}
