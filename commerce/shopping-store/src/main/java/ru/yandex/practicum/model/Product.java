package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.QuantityState;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductState;

import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products", schema = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Column(name = "product_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Column(name = "product_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column
    private Long rating;

    @Column
    private Double price;
}