package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.dto.CartDto;
import ru.yandex.practicum.model.Cart;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "products", source = "cart.cartProducts")
    CartDto toDto(Cart cart);

    @Mapping(target = "cartProducts", source = "cartDto.products")
    Cart toCart(CartDto cartDto);
}
