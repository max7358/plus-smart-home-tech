package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.DimensionMapper;
import ru.yandex.practicum.model.Warehouse;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class WarehouseService {
    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public void putProduct(NewProductInWarehouseRequest request) {
        Optional<Warehouse> product = warehouseRepository.findByProductId(request.getProductId());
        if (product.isPresent()) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already exists in warehouse");
        } else {
            Warehouse warehouse = new Warehouse();
            warehouse.setProductId(request.getProductId());
            warehouse.setFragile(request.isFragile());
            warehouse.setDimension(DimensionMapper.INSTANCE.toDimension(request.getDimension()));
            warehouse.setWeight(request.getWeight());
            warehouseRepository.save(warehouse);
        }
    }

    @Transactional
    public void addProduct(AddProductToWarehouseRequest request) {
        Warehouse product = warehouseRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Product not found"));
        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseRepository.save(product);
    }

    public AddressDto getAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    private List<Warehouse> findProductsById(Set<UUID> productsId) {
        List<Warehouse> products = warehouseRepository.findByProductIdIn(productsId);
        if (products.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Product not found in warehouse");
        }
        return products;
    }

    public BookedProductsDto checkQuantity(CartDto cartDto) {
        List<Warehouse> products = findProductsById(cartDto.getProducts().keySet());
        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        for (Warehouse product : products) {
            if (product.getQuantity() < cartDto.getProducts().get(product.getProductId())) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Product quantity in shopping cart is more than in warehouse");
            }
            if (product.isFragile()) {
                bookedProductsDto.setFragile(true);
            }
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() +
                    product.getDimension().getHeight() * product.getDimension().getDepth() * product.getDimension().getWidth());
            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() +
                    product.getWeight());
        }
        return bookedProductsDto;
    }

    public void returnProducts(Map<UUID, Integer> products) {
        List<Warehouse> warehouseProducts = findProductsById(products.keySet());
        warehouseProducts.forEach(warehouseProduct
                -> warehouseProduct.setQuantity(warehouseProduct.getQuantity() + products.get(warehouseProduct.getProductId())));
        warehouseRepository.saveAll(warehouseProducts);
    }

    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        List<Warehouse> warehouseProducts = findProductsById(request.getProducts().keySet());
        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        for (Warehouse product : warehouseProducts) {
            if (product.getQuantity() < request.getProducts().get(product.getProductId())) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Product quantity in shopping cart is more than in warehouse");
            }
            if (product.isFragile()) {
                bookedProductsDto.setFragile(true);
            }
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() +
                    product.getDimension().getHeight() * product.getDimension().getDepth() * product.getDimension().getWidth());
            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() +
                    product.getWeight());
        }
        return bookedProductsDto;
    }
}
