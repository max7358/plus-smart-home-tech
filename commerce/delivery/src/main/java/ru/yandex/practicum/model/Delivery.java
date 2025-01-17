package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery", schema = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column (name = "delivery_id")
    private UUID deliveryId;
    @OneToOne
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private Address fromAddress;
    @OneToOne
    @JoinColumn(name = "to_id", referencedColumnName = "id")
    private Address toAddress;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;
    @Column(name = "delivery_weight")
    private Double deliveryWeight;
    @Column(name = "delivery_volume")
    private Double deliveryVolume;
    @Column
    private Boolean fragile;
}