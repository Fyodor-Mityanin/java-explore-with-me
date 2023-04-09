package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.EventFullDto;
import ru.practicum.ewm.entity.dto.EventShortDto;
import ru.practicum.ewm.entity.dto.Location;
import ru.practicum.ewm.entity.dto.NewEventDto;
import ru.practicum.ewm.entity.enums.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventMapper {

    public static Event toObject(Category category, User initiator, NewEventDto dto, State state) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setCategory(category);
        event.setEventDate(dto.getEventDate());
        event.setInitiator(initiator);
        event.setLatitude(dto.getLocation().getLat());
        event.setLongitude(dto.getLocation().getLon());
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setState(state);
        return event;
    }

    public static EventFullDto toDto(Event obj) {
        return EventFullDto.builder()
                .id(obj.getId())
                .title(obj.getTitle())
                .annotation(obj.getAnnotation())
                .description(obj.getDescription())
                .category(CategoryMapper.toDto(obj.getCategory()))
                .createdOn(obj.getCreated())
                .eventDate(obj.getEventDate())
                .initiator(UserMapper.toShortDto(obj.getInitiator()))
                .location(new Location(obj.getLatitude(), obj.getLongitude()))
                .paid(obj.getPaid())
                .participantLimit(obj.getParticipantLimit())
                .publishedOn(obj.getPublishedOn())
                .requestModeration(obj.getRequestModeration())
                .state(obj.getState())
                .build();
    }

    public static List<EventFullDto> toDtos(List<Event> objs) {
        List<EventFullDto> dtos = new ArrayList<>();
        objs.forEach(i -> dtos.add(toDto(i)));
        return dtos;
    }

    public static EventShortDto toShortDto(Event obj) {
        return EventShortDto.builder()
                .id(obj.getId())
                .annotation(obj.getAnnotation())
                .category(CategoryMapper.toDto(obj.getCategory()))
                .confirmedRequest(0)
                .eventDate(obj.getEventDate())
                .initiator(UserMapper.toShortDto(obj.getInitiator()))
                .paid(obj.getPaid())
                .title(obj.getTitle())
                .views(0)
                .build();
    }

    public static List<EventShortDto> toShortDtos(Set<Event> objs) {
        return objs.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }
}
