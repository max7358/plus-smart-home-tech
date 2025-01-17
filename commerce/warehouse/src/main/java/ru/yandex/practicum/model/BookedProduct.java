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
@Table(name = "booking", schema = "warehouse")
public class BookedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;
    @Column
    private double deliveryWeight;
    @Column
    private double deliveryVolume;
    @Column
    private boolean fragile;
    @Column
    private UUID orderId;
    @Column
    private UUID deliveryId;
}
