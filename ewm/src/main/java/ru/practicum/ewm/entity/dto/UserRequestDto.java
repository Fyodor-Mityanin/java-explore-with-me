package ru.practicum.ewm.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @Size(max = 255)
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 255)
    @NotNull
    @Email
    private String email;
}
