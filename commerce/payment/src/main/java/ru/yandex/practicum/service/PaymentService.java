package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.PaymentStatus;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class PaymentService {

    private static final Double VAT = 0.1;
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    public PaymentService(PaymentRepository paymentRepository, OrderClient orderClient) {
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
    }

    @Transactional
    public PaymentDto createPayment(OrderDto orderDto) {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        return PaymentMapper.INSTANCE.toDto(paymentRepository.save(payment));
    }

    @Transactional
    public Double calculateTotalCost(OrderDto orderDto) {
        Double deliveryPrice = orderDto.getDeliveryPrice();
        Double productPrice = orderDto.getProductPrice();
        Double totalPrice = orderDto.getTotalPrice();
        if (deliveryPrice == null || productPrice == null) {
            throw new NotEnoughInfoInOrderToCalculateException("No delivery price or product price");
        }
        Payment payment = paymentRepository.findByPaymentId(orderDto.getPaymentId()).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        payment.setOrderId(orderDto.getOrderId());
        payment.setTotalPayment(totalPrice);
        payment.setFeeTotal(productPrice * VAT);
        payment.setDeliveryTotal(deliveryPrice);
        payment.setTotalPayment(totalPrice);
        paymentRepository.save(payment);
        return payment.getTotalPayment();
    }

    @Transactional
    public Double calculateProductCost(OrderDto orderDto) {
        paymentRepository.findByPaymentId(orderDto.getPaymentId()).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        return orderDto.getProductPrice() + orderDto.getProductPrice() / VAT;
    }

    @Transactional
    public void failedPayment(UUID orderId) {
        Payment payment = paymentRepository.findByPaymentId(orderId).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        orderClient.payFail(orderId);
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    //success payment
    @Transactional
    public void refundPayment(UUID orderId) {
        Payment payment = paymentRepository.findByPaymentId(orderId).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        orderClient.payOrder(orderId);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }
}
