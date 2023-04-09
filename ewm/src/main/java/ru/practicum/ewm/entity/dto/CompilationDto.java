package ru.practicum.ewm.entity.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.entity.Compilation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link Compilation} entity
 */
@Data
@Builder
public class CompilationDto implements Serializable {
    private final Long id;
    @Size(max = 255)
    @NotNull
    private final String title;
    @NotNull
    private final Boolean pinned;
    private final List<EventShortDto> events;
}