package ru.practicum.ewm.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewCategoryDto {
    @Size(max = 255)
    @NotNull
    @NotBlank
    private String name;
}
