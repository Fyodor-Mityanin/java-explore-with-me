package ru.practicum.ewm.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private final Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime eventDate;
    private final UserShortDto initiator;
    private final Boolean paid;
    private final String title;
    private Long views;
}