package ru.practicum.ewm.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Location implements Serializable {
    private Double lat;
    private Double lon;
}
