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
import ru.practicum.ewm.entity.enums.State;
import ru.practicum.ewm.entity.mapper.ParticipationMapper;
import ru.practicum.ewm.error.exeptions.ConflictException;
import ru.practicum.ewm.error.exeptions.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;
import java.util.Objects;

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
        if (event.getState() == State.PENDING) {
            throw new ConflictException("Event is PENDING");
        }
        if (Objects.equals(event.getInitiator().getId(), requester.getId())) {
            throw new ConflictException("Initiator cant be requester");
        }
        if (participationRepository.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            throw new ConflictException("Event is full");
        }
        Participation participation;
        if (event.getRequestModeration()) {
            participation = ParticipationMapper.toObject(event, requester, RequestStatus.PENDING);
        } else {
            participation = ParticipationMapper.toObject(event, requester, RequestStatus.CONFIRMED);
        }
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
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
        int left = event.getParticipantLimit() - participationRepository.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (left < updateRequest.getRequestIds().size()) {
            throw new ConflictException("Event is full");
        }
        List<Participation> participations = participationRepository.findByIdIn(updateRequest.getRequestIds());
        participations.forEach(participation -> participation.setStatus(updateRequest.getStatus()));
        participations = participationRepository.saveAll(participations);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(ParticipationMapper.toDtos(participations))
                .rejectedRequests(ParticipationMapper.toDtos(participations))
                .build();

    }

    public List<ParticipationRequestDto> getParticipationsByUserId(Long userId) {
        List<Participation> participations = participationRepository.findByRequester_Id(userId);
        return ParticipationMapper.toDtos(participations);
    }

    public List<ParticipationRequestDto> getParticipationsByUserIdAndEventId(Long eventId) {
        List<Participation> participations = participationRepository.findByEvent_Id(eventId);
        return ParticipationMapper.toDtos(participations);
    }

    public ParticipationRequestDto cancelParticipation(Long userId, Long requestId) {
        Participation participation = participationRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Participation with id=" + requestId + " was not found")
        );
        if (!Objects.equals(participation.getRequester().getId(), userId)) {
            throw new ConflictException("Forbidden");
        }
        participation.setStatus(RequestStatus.CANCELED);
        participation = participationRepository.save(participation);
        return ParticipationMapper.toDto(participation);
    }
}
