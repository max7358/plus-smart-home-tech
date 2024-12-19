package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.QuantityState;
import ru.yandex.practicum.service.StoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@Slf4j
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{id}")
    ProductDto getProduct(@PathVariable UUID id) {
        return storeService.getProduct(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto addProduct(@Valid @RequestBody ProductDto product) {
        return storeService.addProduct(product);
    }

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto product) {
        return storeService.updateProduct(product);
    }

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody UUID uuid) {
        return storeService.removeProduct(uuid);
    }

    @PostMapping("/quantityState")
    boolean setQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
     return storeService.setQuantityState(productId, quantityState);
    }

    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return storeService.getProducts(category, pageable);
    }
}
