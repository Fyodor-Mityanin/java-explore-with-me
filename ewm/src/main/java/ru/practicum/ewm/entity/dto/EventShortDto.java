package ru.practicum.ewm.entity.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.entity.Event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Event} entity
 */
@Data
@Builder
public class EventShortDto implements Serializable {
    private final Long id;
    private final String annotation;
    private final CategoryDto category;
    private final Integer confirmedRequest;
    private final LocalDateTime eventDate;
    private final UserShortDto initiator;
    private final Boolean paid;
    private final String title;
    private final Integer views;
}