package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {
    private double width;
    private double height;
    private double depth;
}
