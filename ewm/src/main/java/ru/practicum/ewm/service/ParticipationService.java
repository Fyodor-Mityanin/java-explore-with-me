package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Participation;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.entity.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.entity.dto.ParticipationRequestDto;
import ru.practicum.ewm.entity.enums.RequestStatus;
import ru.practicum.ewm.entity.mapper.ParticipationMapper;
import ru.practicum.ewm.error.exeptions.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;

@Service
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ParticipationService(
            ParticipationRepository participationRepository,
            UserRepository userRepository,
            EventRepository eventRepository
    ) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public ParticipationRequestDto createParticipation(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        Participation participation = ParticipationMapper.toObject(event, requester, RequestStatus.PENDING);
        participation = participationRepository.save(participation);
        return ParticipationMapper.toDto(participation);
    }


    public EventRequestStatusUpdateResult updateRequestStatus(
            EventRequestStatusUpdateRequest updateRequest,
            Long userId,
            Long eventId
    ) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found")
        );
        eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        List<Participation> participations = participationRepository.findByIdIn(updateRequest.getRequestIds());
        participations.forEach(participation -> participation.setStatus(updateRequest.getStatus()));
        participations = participationRepository.saveAll(participations);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(ParticipationMapper.toDtos(participations))
                .rejectedRequests(ParticipationMapper.toDtos(participations))
                .build();

    }
}
