package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(UUID productId);

    Page<Product> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);
}