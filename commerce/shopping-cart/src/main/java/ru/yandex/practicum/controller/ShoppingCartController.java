package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.feign.ShoppingCartClient;
import ru.yandex.practicum.service.CartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartClient {

    private final CartService cartService;

    @Override
    public ShoppingCartDto getCart(String username) {
        return cartService.getCart(username);
    }

    @Override
    public ShoppingCartDto addProductToCart(
            String username,
            @Valid @NotNull @NotEmpty Map<@NotNull UUID, @NotNull @Positive Long> products
    ) {
        return cartService.addProductToCart(username, products);
    }

    @Override
    public void deleteCart(String username) {
        cartService.deleteCart(username);
    }

    @Override
    public ShoppingCartDto removeFromCart(String username, Set<UUID> productIds) {
        return cartService.removeFromCart(username, productIds);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, @Valid ChangeProductQuantityRequest request) {
        return cartService.changeProductQuantity(username, request);
    }
}