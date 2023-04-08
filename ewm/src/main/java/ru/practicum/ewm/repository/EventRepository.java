package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}