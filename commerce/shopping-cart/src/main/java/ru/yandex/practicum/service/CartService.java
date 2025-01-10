package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.BadRequestException;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.Cart;
import ru.yandex.practicum.repository.CartRepository;


import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final WarehouseClient client;

    @Transactional
    public CartDto addProductsToCart(String username, Map<UUID, Integer> products) {
        Cart cart = getOrCreateCart(username);
        if (!cart.isActive()) {
            throw new BadRequestException("Cart is deactivated");
        }
        cart.getCartProducts().putAll(products);
        client.checkQuantity(CartMapper.INSTANCE.toDto(cart));
        return CartMapper.INSTANCE.toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto getCart(String username) {
        return CartMapper.INSTANCE.toDto(getOrCreateCart(username));
    }

    private Cart getOrCreateCart(String username) {
        Optional<Cart> cart = cartRepository.findByUserName(username);
        if (cart.isPresent()) {
            return cart.get();
        }
        Cart newCart = Cart.builder()
                .userName(username)
                .cartProducts(new HashMap<>())
                .active(true)
                .build();
        return cartRepository.save(newCart);
    }

    @Transactional
    public void deleteCart(String username) {
        Cart cart = cartRepository.findByUserName(username).orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.setActive(false);
        cartRepository.save(cart);
    }

    @Transactional
    public CartDto removeProductFromCart(String username, List<UUID> products) {
        Cart cart = cartRepository.findByUserName(username).orElseThrow(() -> new NotFoundException("Cart not found"));
        if (!cart.isActive()) {
            throw new BadRequestException("Cart is deactivated");
        }
        if (!cart.getCartProducts().keySet().containsAll(products)) {
            throw new NoProductsInShoppingCartException("Some products are not in the cart");
        }
        products.forEach(cart.getCartProducts()::remove);
        return CartMapper.INSTANCE.toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        Cart cart = cartRepository.findByUserName(username).orElseThrow(() -> new NotFoundException("Cart not found"));
        if (!cart.isActive()) {
            throw new BadRequestException("Cart is deactivated");
        }
        if (!cart.getCartProducts().containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException("Products is not in the cart");
        }

        cart.getCartProducts().put(request.getProductId(), request.getNewQuantity());
        return CartMapper.INSTANCE.toDto(cartRepository.save(cart));
    }
}
