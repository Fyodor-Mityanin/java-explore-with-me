package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.dtos.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select eh.app, eh.uri, count(*) as hits " +
            "from endpoint_hits eh " +
            "where eh.hit_time between :start and :end " +
            "group by eh.app, eh.uri, case when :unique then eh.ip end ", nativeQuery = true)
    List<StatisticDto> getStatisticUnique(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
