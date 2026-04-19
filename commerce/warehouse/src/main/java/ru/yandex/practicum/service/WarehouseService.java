package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;

public interface WarehouseService {
    void createNewProductInWarehouse(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantity(ShoppingCartDto cart);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request);

    void acceptReturn(java.util.Map<java.util.UUID, java.lang.Long> products);

    void shipToDelivery(ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest request);
}
