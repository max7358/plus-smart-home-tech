package ru.yandex.practicum.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID,Integer> products;
}
