package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@Slf4j
public class WarehouseController implements WarehouseClient {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    @PutMapping()
    public void putProduct(@RequestBody NewProductInWarehouseRequest request) {
        warehouseService.putProduct(request);

    }

    @PostMapping("/add")
    public void addProduct(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkQuantity(@RequestBody CartDto cart) {
        return warehouseService.checkQuantity(cart);
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        warehouseService.returnProducts(products);
    }

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
       return warehouseService.assembly(request);
    }

    @Override
    public void shipped(ShippedToDeliveryRequest request) {
        warehouseService.shipped(request);
    }
}