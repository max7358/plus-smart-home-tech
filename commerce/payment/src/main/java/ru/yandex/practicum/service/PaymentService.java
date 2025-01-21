package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.StoreClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.PaymentStatus;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
public class PaymentService {

    private static final Double VAT = 0.1;
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final StoreClient storeClient;

    public PaymentService(PaymentRepository paymentRepository, OrderClient orderClient, StoreClient storeClient) {
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
        this.storeClient = storeClient;
    }

    @Transactional
    public PaymentDto createPayment(OrderDto orderDto) {
        Double deliveryPrice = orderDto.getDeliveryPrice();
        Double productPrice = orderDto.getProductPrice();
        Double totalPrice = orderDto.getTotalPrice();
        Payment payment = new Payment();
        payment.setOrderId(orderDto.getOrderId());
        payment.setFeeTotal(productPrice * VAT);
        payment.setDeliveryTotal(deliveryPrice);
        payment.setTotalPayment(totalPrice);
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentRepository.save(payment);
        Payment save = paymentRepository.save(payment);
        log.info("Payment for order {} is created", orderDto.getOrderId());
        return PaymentMapper.INSTANCE.toDto(save);
    }


    public Double calculateTotalCost(OrderDto orderDto) {
        Double deliveryPrice = orderDto.getDeliveryPrice();
        Double productPrice = calculateProductCost(orderDto);
        if (deliveryPrice == null || productPrice == null) {
            throw new NotEnoughInfoInOrderToCalculateException("No delivery price or product price");
        }
        return productPrice + productPrice * VAT + orderDto.getDeliveryPrice();
    }

    public Double calculateProductCost(OrderDto orderDto) {
        paymentRepository.findByPaymentId(orderDto.getPaymentId()).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        Map<UUID, Integer> products = orderDto.getProducts();
        double productPrice = 0;
        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            ProductDto productDto = storeClient.getProduct(entry.getKey());
            if (productDto == null) {
                throw new NotFoundException("Product not found in store");
            }
            productPrice += productDto.getPrice() * products.get(entry.getKey());
        }
        return productPrice;
    }

    @Transactional
    public void failedPayment(UUID orderId) {
        Payment payment = paymentRepository.findByPaymentId(orderId).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        orderClient.payFail(orderId);
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        log.info("Payment for order {} is failed", orderId);
    }

    //success payment
    @Transactional
    public void refundPayment(UUID orderId) {
        Payment payment = paymentRepository.findByPaymentId(orderId).orElseThrow(() -> new NoPaymentFoundException("Payment not found"));
        orderClient.payOrder(orderId);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        log.info("Payment for order {} is success", orderId);
    }
}
