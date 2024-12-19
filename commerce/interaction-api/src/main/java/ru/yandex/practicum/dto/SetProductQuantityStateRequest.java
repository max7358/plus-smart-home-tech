package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequest {
    @org.hibernate.validator.constraints.UUID
    private UUID productId;
    @NotNull
    private QuantityState quantityState;
}
