package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/delivery")
@Slf4j
public class DeliveryController implements DeliveryClient {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    public void success(UUID orderId) {
        deliveryService.success(orderId);
    }

    @Override
    public void picked(UUID orderId) {
        deliveryService.picked(orderId);
    }

    @Override
    public void failed(UUID orderId) {
        deliveryService.failed(orderId);
    }

    @Override
    public Double cost(OrderDto orderDto) {
        return deliveryService.cost(orderDto);
    }
}
