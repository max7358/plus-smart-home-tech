package ru.yandex.practicum.mapper;

import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    Product toProduct(ProductDto productDto);
}
