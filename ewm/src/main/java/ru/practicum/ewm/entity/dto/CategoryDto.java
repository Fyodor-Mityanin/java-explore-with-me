package ru.practicum.ewm.entity.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewm.entity.Category} entity
 */
@Data
@Builder
public class CategoryDto implements Serializable {
    private final Long id;
    @Size(max = 255)
    private final String name;
}