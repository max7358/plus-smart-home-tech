package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.OrderState;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders", schema = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;
    @Column
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"), schema = "orders")
    @MapKeyColumn(name = "product_id")
    @Column(name = "count")
    private Map<UUID, Integer> products;

    @Column
    private UUID paymentId;
    @Column
    private UUID deliveryId;
    @Column
    private OrderState state;
    @Column
    private Double deliveryWeight;
    @Column
    private Double deliveryVolume;
    @Column
    private Boolean fragile;
    @Column
    private Double totalPrice;
    @Column
    private Double deliveryPrice;
    @Column
    private Double productPrice;
    @Column
    private String userName;
    @Embedded
    private Address address;
}