package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Warehouse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByProductId(UUID productId);

    List<Warehouse> findByProductIdIn(Set<UUID> uuids);
}