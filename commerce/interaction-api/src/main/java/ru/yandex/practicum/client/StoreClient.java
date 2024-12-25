package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.QuantityState;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface StoreClient {
    @GetMapping("/{id}")
    ProductDto getProduct(@PathVariable UUID id);

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto addProduct(@Valid @RequestBody ProductDto product);

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody UUID uuid);

    @PostMapping("/quantityState")
    boolean setQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @GetMapping
    Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);
}
