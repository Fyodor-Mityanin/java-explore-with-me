package ru.practicum.model.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Builder
public class EndpointHitDto {

    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;

    @NotNull
    @NotBlank
    private String ip;

    @PastOrPresent
    private LocalDateTime hitTime;
}
