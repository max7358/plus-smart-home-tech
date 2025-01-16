package ru.yandex.practicum.dto;

import lombok.Data;

@Data
public class CreateNewOrderRequest {
    private CartDto shoppingCartDto;
    private AddressDto addressDto;
}
