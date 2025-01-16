package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@Slf4j
public class OrderController implements OrderClient {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public OrderDto getOrder(String userName) {
        return orderService.getOrder(userName);
    }

    @Override
    public OrderDto createOrder(String userName, CreateNewOrderRequest request) {
        return orderService.createOrder(userName, request);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        return orderService.payOrder(orderId);
    }

    @Override
    public OrderDto payFail(UUID orderId) {
        return orderService.payFail(orderId);
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        return orderService.deliverOrder(orderId);
    }

    @Override
    public OrderDto deliveryFail(UUID orderId) {
        return orderService.deliveryFail(orderId);
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        return orderService.completeOrder(orderId);
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        return orderService.calculateTotal(orderId);
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        return orderService.assembleOrder(orderId);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }
}
