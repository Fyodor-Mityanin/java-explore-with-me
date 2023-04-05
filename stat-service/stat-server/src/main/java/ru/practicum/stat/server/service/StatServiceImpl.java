package ru.practicum.stat.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.client.dtos.StatisticDto;
import ru.practicum.stat.server.model.EndpointHit;
import ru.practicum.stat.server.model.dtos.EndpointHitMapper;
import ru.practicum.stat.server.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void createHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toObject(endpointHitDto);
        statRepository.save(endpointHit);
    }

    public List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return statRepository.getStatistic(start, end, uris, unique);
    }
}
