package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToWarehouseRequest {
    @org.hibernate.validator.constraints.UUID
    private UUID productId;
    @Min(1)
    private int quantity;
}
