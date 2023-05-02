package ru.practicum.ewm.entity.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewm.entity.User} entity
 */
@Data
@Builder
public class UserDto implements Serializable {
    private final Long id;
    @Size(max = 255)
    @NotNull
    @NotBlank
    private final String name;
    @Size(max = 255)
    @NotNull
    @Email
    private final String email;
}