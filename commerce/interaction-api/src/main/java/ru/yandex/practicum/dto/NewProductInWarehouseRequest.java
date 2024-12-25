package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {
    @org.hibernate.validator.constraints.UUID
    private UUID productId;
    private boolean fragile;
    private DimensionDto dimension;
    private double weight;
}
