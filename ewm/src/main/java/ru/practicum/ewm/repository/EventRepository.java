package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.entity.Event;

import java.util.Collection;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Set<Event> findByIdIn(Collection<Long> ids);

    Page<Event> findByInitiator_Id(Long initiatorId, Pageable pageable);

    boolean existsByCategory_id(Long catId);
}