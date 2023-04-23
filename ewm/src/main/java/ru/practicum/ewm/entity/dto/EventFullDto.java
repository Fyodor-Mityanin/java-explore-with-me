package ru.practicum.ewm.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.enums.State;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Event} entity
 */
@Data
@Builder
public class EventFullDto implements Serializable {
    private final Long id;
    private final String title;
    private final String annotation;
    private final String description;
    private final CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime eventDate;
    private final UserShortDto initiator;
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final LocalDateTime publishedOn;
    private final Boolean requestModeration;
    private final State state;
    private final Integer views;
    private final Integer confirmedRequests;
}