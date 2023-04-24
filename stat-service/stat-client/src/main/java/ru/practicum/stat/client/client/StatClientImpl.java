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
import java.util.List;

@Component
public class StatClientImpl implements StatClient {

    public static final String SERVICE_URL = "http://localhost:9090";

    private final RestTemplate restTemplate;

    public StatClientImpl() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restTemplate = builder.build();
    }

    @Override
    public ResponseEntity<Void> hit(EndpointHitDto endpointHitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);
        return restTemplate.exchange(
                SERVICE_URL + "/hit",
                HttpMethod.POST,
                request,
                Void.class
        );
    }

    @Override
    public List<StatisticDto> stats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(SERVICE_URL + "/stats")
                .queryParam("start", start.toString())
                .queryParam("end", end.toString())
                .queryParam("unique", unique.toString())
                .queryParam("uris", String.join(",", uris));
        ResponseEntity<List<StatisticDto>> responseEntity =
                restTemplate.exchange(
                        uriBuilder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        return responseEntity.getBody();
    }
}
