package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    public void createNewProductInWarehouse(@Valid NewProductInWarehouseRequest request) {
        warehouseService.createNewProductInWarehouse(request);
    }

    @Override
    public BookedProductsDto checkProductQuantity(@Valid ShoppingCartDto cart) {
        return warehouseService.checkProductQuantity(cart);
    }

    @Override
    public void addProductToWarehouse(@Valid AddProductToWarehouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }

    @Override
    public ru.yandex.practicum.dto.warehouse.BookedProductsDto assembleProducts(@jakarta.validation.Valid ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest request) {
        return warehouseService.assembleProducts(request);
    }

    @Override
    public void acceptReturn(java.util.Map<java.util.UUID, java.lang.Long> products) {
        warehouseService.acceptReturn(products);
    }

    @Override
    public void shipToDelivery(ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest request) {
        warehouseService.shipToDelivery(request);
    }
}
