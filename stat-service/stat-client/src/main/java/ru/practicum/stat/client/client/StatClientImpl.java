package ru.practicum.stat.client.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.client.dtos.StatisticDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatClientImpl implements StatClient {
    private final RestTemplate restTemplate;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");

    public StatClientImpl() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restTemplate = builder.build();
    }

    @Override
    public ResponseEntity<Void> hit(EndpointHitDto endpointHitDto, String serviceUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);
        return restTemplate.exchange(
                serviceUrl + "/hit",
                HttpMethod.POST,
                request,
                Void.class
        );
    }

    @Override
    public List<StatisticDto> stats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, String serviceUrl) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serviceUrl + "/stats")
                .queryParam("start", start.format(dtf))
                .queryParam("end", end.format(dtf))
                .queryParam("unique", unique.toString())
                .queryParam("uris", String.join(",", uris));
        String uri = uriBuilder.toUriString();
        ResponseEntity<List<StatisticDto>> responseEntity =
                restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        return responseEntity.getBody();
    }
}
