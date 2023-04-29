package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.stat.client.client.StatClient;
import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.client.dtos.StatisticDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticService {

    @Value("${app.name}")
    private String appName;

    @Value("${app.serviceUrl}")
    private String serviceUrl;

    private final StatClient statClient;

    @Autowired
    public StatisticService(StatClient statClient) {
        this.statClient = statClient;
    }

    public void hit(HttpServletRequest httpRequest) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(appName);
        endpointHitDto.setIp(httpRequest.getRemoteAddr());
        endpointHitDto.setUri(httpRequest.getRequestURI());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        log.info("Sending hit {} from {}", httpRequest.getRequestURI(), httpRequest.getRemoteAddr());
        ResponseEntity<Void> response = statClient.hit(endpointHitDto, serviceUrl);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            log.info("Hit success");
        } else {
            log.warn("Something went wrong");
        }
    }

    public Map<Long, Long> getEventViews(Set<Long> eventIds) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusYears(1);
        List<String> uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());
        List<StatisticDto> stats = statClient.stats(end, start, uris, true, serviceUrl);
        return stats.stream()
                .collect(
                        Collectors.toMap(
                                i -> {
                                    String[] urlParts = i.getUri().split("/");
                                    return Long.valueOf(urlParts[urlParts.length-1]);
                                },
                                StatisticDto::getHits
                        )
                );
    }
}
