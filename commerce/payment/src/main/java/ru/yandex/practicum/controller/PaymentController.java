package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    @Override
    public Double calculateTotalCost(OrderDto orderDto) {
        return paymentService.calculateTotalCost(orderDto);
    }

    @Override
    public void refundPayment(UUID orderId) {
        paymentService.refundPayment(orderId);
    }

    @Override
    public Double calculateProductCost(OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    @Override
    public void failedPayment(UUID orderId){
    paymentService.failedPayment(orderId);
    }
}
