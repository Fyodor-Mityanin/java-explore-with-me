package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Participation;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.ParticipationRequestDto;
import ru.practicum.ewm.entity.enums.Status;

public class ParticipationMapper {
    public static Participation toObject(Event event, User requester, Status pending) {
        Participation participation = new Participation();
        participation.setRequester(requester);
        participation.setEvent(event);
        participation.setStatus(pending);
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
}
