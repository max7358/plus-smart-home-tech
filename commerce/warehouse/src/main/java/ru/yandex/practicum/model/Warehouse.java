package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse", schema = "warehouse")
public class Warehouse {
    @Id
    @Column(name = "product_id")
    private UUID productId;

    private boolean fragile;

    @OneToOne(cascade = CascadeType.ALL)
    private Dimension dimension;

    private double weight;

    private int quantity = 0;
}