package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts", schema = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "cart_products", joinColumns = @JoinColumn(name = "shopping_cart_id"), schema = "cart")
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> cartProducts = new HashMap<>();

    @Column(name = "active")
    private boolean active;

    @Column(name = "user_name")
    private String userName;
}