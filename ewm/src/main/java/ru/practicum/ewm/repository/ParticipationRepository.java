package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Participation;
import ru.practicum.ewm.entity.enums.RequestStatus;

import java.util.Collection;
import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByIdIn(Collection<Long> ids);

    int countByEvent_IdAndStatus(Long id, RequestStatus status);

    List<Participation> findByRequester_Id(Long userId);

    List<Participation> findByEvent_Id(Long eventId);

    boolean existsByRequester_IdAndEvent_IdAndStatus(Long requesterId, Long eventId, RequestStatus status);
}