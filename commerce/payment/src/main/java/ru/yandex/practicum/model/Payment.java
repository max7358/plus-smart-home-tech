package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.PaymentStatus;

import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments", schema = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;
    @Column(name = "total_payment")
    private Double totalPayment;
    @Column(name = "delivery_total")
    private Double deliveryTotal;
    @Column(name = "fee_total")
    private Double feeTotal;
    @Column
    private PaymentStatus status;
    @Column(name = "order_id")
    private UUID orderId;
}
