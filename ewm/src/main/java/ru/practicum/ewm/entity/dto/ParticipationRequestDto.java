package ru.practicum.ewm.entity.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.entity.enums.Status;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewm.entity.Participation} entity
 */
@Data
@Builder
public class ParticipationRequestDto implements Serializable {
    private final Long id;
    private final Long requester;
    private final Long event;
    private final LocalDateTime created;
    private final Status status;
}