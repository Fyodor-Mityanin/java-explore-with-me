package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}