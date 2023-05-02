package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Participation;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.ParticipationRequestDto;
import ru.practicum.ewm.entity.enums.RequestStatus;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipationMapper {
    public static Participation toObject(Event event, User requester, RequestStatus status) {
        Participation participation = new Participation();
        participation.setRequester(requester);
        participation.setEvent(event);
        participation.setStatus(status);
        return participation;
    }

    public static ParticipationRequestDto toDto(Participation participation) {
        return ParticipationRequestDto.builder()
                .id(participation.getId())
                .requester(participation.getRequester().getId())
                .event(participation.getEvent().getId())
                .created(participation.getCreated())
                .status(participation.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toDtos(List<Participation> participations) {
        return participations.stream().map(ParticipationMapper::toDto).collect(Collectors.toList());
    }
}
