package ru.practicum.stat.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.client.dtos.StatisticDto;
import ru.practicum.stat.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(name = "get_statistic", nativeQuery = true)
    List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
