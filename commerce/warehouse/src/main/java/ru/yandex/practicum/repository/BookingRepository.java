package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.BookedProduct;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookedProduct, Long> {
    Optional<BookedProduct> findByOrderId(UUID orderId);
}