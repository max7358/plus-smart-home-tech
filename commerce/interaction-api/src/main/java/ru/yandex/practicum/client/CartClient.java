package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CartDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartClient {

    @GetMapping
    CartDto getCart(@RequestParam @Valid @NotBlank String username);

    @PutMapping
    CartDto addProductsToCart(@RequestParam @Valid @NotBlank String username, @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deleteCart(@RequestParam @Valid @NotBlank String username);

    @PostMapping("/remove")
    CartDto removeProductFromCart(@RequestParam @Valid @NotBlank String username, @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    CartDto changeProductQuantity(@RequestParam @Valid @NotBlank String username, @RequestBody ChangeProductQuantityRequest request);
}
