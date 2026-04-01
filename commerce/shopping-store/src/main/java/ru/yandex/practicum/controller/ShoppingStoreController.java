package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.QuantityState;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.service.StoreService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController implements ShoppingStoreClient {

    private final StoreService storeService;

    @Override
    @GetMapping
    public Page<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, Pageable pageable) {
        return storeService.getProductsByCategory(category, pageable);
    }

    @Override
    @PutMapping
    public ProductDto addProduct(@RequestBody ProductDto productDto) {
        return storeService.addProduct(productDto);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return storeService.updateProduct(productDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public Boolean removeProduct(@RequestBody UUID productId) {
        return storeService.removeProduct(productId);
    }

    @Override
    @PostMapping("/quantityState")
    public Boolean updateQuantityState(@RequestParam UUID productId,
                                       @RequestParam QuantityState quantityState) {
        return storeService.updateQuantityState(productId, quantityState);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        return storeService.getProductById(productId);
    }
}