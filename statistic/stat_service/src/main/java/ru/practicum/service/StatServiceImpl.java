package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.dtos.EndpointHitDto;
import ru.practicum.model.dtos.EndpointHitMapper;
import ru.practicum.model.dtos.StatisticDto;
import ru.practicum.repository.StatRepository;

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
        return statRepository.getStatisticUnique(start, end, uris, unique);
    }
}
