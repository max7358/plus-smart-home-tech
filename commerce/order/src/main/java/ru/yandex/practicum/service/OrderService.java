package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseClient warehouseClient;

    public OrderService(OrderRepository orderRepository, WarehouseClient warehouseClient) {
        this.orderRepository = orderRepository;
        this.warehouseClient = warehouseClient;
    }

    public OrderDto getOrder(String userName) {
        Order order = orderRepository.findByUserName(userName).orElseThrow(() -> new NotFoundException("Order not found"));
        return OrderMapper.INSTANCE.toDto(order);
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Transactional
    public OrderDto createOrder(String userName, CreateNewOrderRequest request) {
        Order order = new Order();
        order.setUserName(userName);
        Map<UUID, Integer> products = request.getShoppingCartDto().getProducts().keySet().stream()
                .collect(Collectors.toMap(id -> id, id -> request.getShoppingCartDto().getProducts().get(id), (a, b) -> b));
        order.setProducts(products);
        order.setShoppingCartId(request.getShoppingCartDto().getShoppingCartId());
        order.setAddress(AddressMapper.INSTANCE.toAddress(request.getAddressDto()));
        order.setState(OrderState.NEW);
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = getOrder(request.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto payOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAID);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto payFail(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto deliverOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto deliveryFail(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto assembleOrder(UUID orderId) {
        Order order = getOrder(orderId);
        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest();
        request.setProducts(order.getProducts());
        request.setOrderId(orderId);
        warehouseClient.assembly(request);
        order.setState(OrderState.ASSEMBLED);

        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto assemblyFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto calculateDelivery(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ON_DELIVERY);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto calculateTotal(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ON_PAYMENT);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }
    @Transactional
    public OrderDto completeOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.COMPLETED);
        warehouseClient.returnProducts(order.getProducts());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }
}
