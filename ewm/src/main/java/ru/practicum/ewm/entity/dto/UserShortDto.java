package ru.practicum.ewm.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewm.entity.User} entity
 */
@Data
@Builder
public class UserShortDto implements Serializable {
    private final Long id;
    private final String name;
}