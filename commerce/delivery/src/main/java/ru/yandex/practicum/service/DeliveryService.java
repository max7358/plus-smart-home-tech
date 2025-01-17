package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;
    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    public DeliveryService(DeliveryRepository deliveryRepository, OrderClient orderClient, WarehouseClient warehouseClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderClient = orderClient;
        this.warehouseClient = warehouseClient;
    }

    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        deliveryDto.setDeliveryState(DeliveryState.CREATED);
        return DeliveryMapper.INSTANCE.toDto(deliveryRepository.save(DeliveryMapper.INSTANCE.toDelivery(deliveryDto)));
    }

    @Transactional
    public void success(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("Delivery not found"));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.completeOrder(orderId);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void failed(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("Delivery not found"));
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.deliveryFail(orderId);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void picked(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("Delivery not found"));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        ShippedToDeliveryRequest request = new ShippedToDeliveryRequest();
        request.setOrderId(orderId);
        request.setDeliveryId(delivery.getDeliveryId());
        warehouseClient.shipped(request);
    }

    public Double cost(OrderDto orderDto) {
        double base = 5.0;
        AddressDto address = warehouseClient.getAddress();
        double cost = base;
        cost = switch (address.getCity()) {
            case "ADDRESS_1" -> cost + cost;
            case "ADDRESS_2" -> cost + cost * 2;
            default -> cost;
        };
        if(Boolean.TRUE.equals(orderDto.getFragile())){
            cost = cost + cost*0.2;
        }
        cost = cost + orderDto.getDeliveryWeight()*0.3;
        cost = cost + orderDto.getDeliveryVolume()*0.2;
        if(!address.getStreet().equals(CURRENT_ADDRESS)){
            cost = cost + cost*0.2;
        }
        return cost;
    }
}
