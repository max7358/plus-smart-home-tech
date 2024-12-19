package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.dto.QuantityState;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.StoreRepository;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        return ProductMapper.INSTANCE.toDto(storeRepository.save(ProductMapper.INSTANCE.toProduct(productDto)));
    }

    public ProductDto getProduct(UUID id) {
        return ProductMapper.INSTANCE.toDto(storeRepository.findByProductId(id)
                .orElseThrow(() -> new NotFoundException("Product not found")));
    }

    @Transactional
    public boolean removeProduct(UUID id) {
        ProductDto product = getProduct(id);
        product.setProductState(ProductState.DEACTIVATE);
        storeRepository.save(ProductMapper.INSTANCE.toProduct(product));
        return true;
    }

    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        ProductDto productToUpdate = getProduct(productDto.getProductId());
        productToUpdate.setProductName(productDto.getProductName());
        productToUpdate.setDescription(productDto.getDescription());
        productToUpdate.setImageSrc(productDto.getImageSrc());
        productToUpdate.setQuantityState(productDto.getQuantityState());
        productToUpdate.setProductState(productDto.getProductState());
        productToUpdate.setProductCategory(productDto.getProductCategory());
        productToUpdate.setRating(productDto.getRating());
        productToUpdate.setPrice(productDto.getPrice());
        return ProductMapper.INSTANCE.toDto(storeRepository.save(ProductMapper.INSTANCE.toProduct(productToUpdate)));
    }

    @Transactional
    public boolean setQuantityState(UUID productId, QuantityState quantityState) {
        ProductDto productDto = getProduct(productId);
        productDto.setQuantityState(quantityState);
        storeRepository.save(ProductMapper.INSTANCE.toProduct(productDto));
        return true;

    }

    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<Product> products = storeRepository.findAllByProductCategory(category, pageable);
        return products.map(ProductMapper.INSTANCE::toDto);
    }
}
