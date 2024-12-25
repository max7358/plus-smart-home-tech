package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.CartClient;
import ru.yandex.practicum.dto.CartDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.service.CartService;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@Slf4j
public class CartController implements CartClient {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping
    public CartDto addProductsToCart(@RequestParam @Valid @NotBlank String username, @RequestBody Map<UUID, Integer> products) {
        return cartService.addProductsToCart(username, products);
    }

    public CartDto getCart(@RequestParam @Valid @NotBlank String username) {
        return cartService.getCart(username);
    }

    @DeleteMapping
    public void deleteCart(@RequestParam @Valid @NotBlank String username) {
        cartService.deleteCart(username);
    }

    @PostMapping("/remove")
    public CartDto removeProductFromCart(@RequestParam @Valid @NotBlank String username, @RequestBody List<UUID> products) {
        return cartService.removeProductFromCart(username, products);
    }

    @PostMapping("/change-quantity")
    public CartDto changeProductQuantity(@RequestParam @Valid @NotBlank String username, @RequestBody ChangeProductQuantityRequest request) {
        return cartService.changeProductQuantity(username, request);
    }
}

