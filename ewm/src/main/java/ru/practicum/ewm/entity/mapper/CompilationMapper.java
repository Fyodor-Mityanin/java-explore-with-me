package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.dto.CompilationDto;
import ru.practicum.ewm.entity.dto.NewCompilationDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toDto(Compilation obj, Map<Long, Long> viewsMap) {
        return CompilationDto.builder()
                .id(obj.getId())
                .pinned(obj.getPinned())
                .title(obj.getTitle())
                .events(EventMapper.toShortDtos(obj.getEvents(), viewsMap))
                .build();
    }

    public static List<CompilationDto> toDtos(List<Compilation> objs, Map<Long, Long> viewsMap) {
        return objs.stream().map(obj -> toDto(obj, viewsMap)).collect(Collectors.toList());
    }

    public static Compilation toObject(NewCompilationDto dto, Set<Event> events) {
        Compilation obj = new Compilation();
        obj.setTitle(dto.getTitle());
        obj.setPinned(dto.getPinned());
        obj.setEvents(events);
        return obj;
    }
}
