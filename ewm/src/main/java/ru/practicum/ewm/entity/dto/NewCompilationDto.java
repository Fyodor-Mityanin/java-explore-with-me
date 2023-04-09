package ru.practicum.ewm.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto implements Serializable {
    private List<Long> events;
    private Boolean pinned = false;
    @NotNull
    @NotBlank
    private String title;
}
