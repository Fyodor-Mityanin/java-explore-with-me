package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.stat.client.client.StatClient;
import ru.practicum.stat.client.client.StatClientImpl;
import ru.practicum.stat.client.dtos.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@Slf4j
public class StatisticService {

    @Value("${app.name}")
    private String appName;

    @Value("${app.serviceUrl}")
    private String serviceUrl;

    private final StatClient statClient;

    public StatisticService() {
        this.statClient = new StatClientImpl();
    }

    public void hit(HttpServletRequest httpRequest) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(appName);
        endpointHitDto.setIp(httpRequest.getRemoteAddr());
        endpointHitDto.setUri(httpRequest.getRequestURI());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        log.info("Sending hit {} from {}", httpRequest.getRequestURI(), httpRequest.getRemoteAddr());
        ResponseEntity<Void> response = statClient.hit(endpointHitDto, serviceUrl);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Hit success");
        } else {
            log.warn("Something went wrong");
        }
    }
}
