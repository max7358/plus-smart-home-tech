package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    @org.hibernate.validator.constraints.UUID
    private UUID shoppingCartId;
    private Map<UUID, Integer> products;
}
