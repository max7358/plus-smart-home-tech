package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

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
}