package ru.practicum.stat.server.model.dtos;

import ru.practicum.stat.client.dtos.EndpointHitDto;
import ru.practicum.stat.server.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit toObject(EndpointHitDto dto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(dto.getApp());
        endpointHit.setUri(dto.getUri());
        endpointHit.setIp(dto.getIp());
        endpointHit.setHitTime(dto.getTimestamp());
        return endpointHit;
    }
}
