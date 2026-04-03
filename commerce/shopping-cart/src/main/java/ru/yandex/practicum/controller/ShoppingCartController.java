package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.feign.ShoppingCartClient;
import ru.yandex.practicum.service.CartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
@Slf4j
@Validated
public class ShoppingCartController implements ShoppingCartClient {
    private final CartService cartService;

    @Override
    public ShoppingCartDto getCart(String username) {
        checkUser(username);
        return cartService.getCart(username);
    }

    @Override
    public ShoppingCartDto addProductToCart(
            String username,
            @Valid @NotNull @NotEmpty Map<@NotNull UUID, @NotNull @Positive Long> products
    ) {
        checkUser(username);
        return cartService.addProductToCart(username, products);
    }

    @Override
    public void deleteCart(String username) {
        checkUser(username);
        cartService.deleteCart(username);
    }

    @Override
    public ShoppingCartDto removeFromCart(
            String username,
            @NotNull @NotEmpty Set<@NotNull UUID> productIds
    ) {
        checkUser(username);
        return cartService.removeFromCart(username, productIds);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, @Valid ChangeProductQuantityRequest request) {
        checkUser(username);
        return cartService.changeProductQuantity(username, request);
    }

    private void checkUser(String username) {
        log.info("Проверка авторизации пользователем");
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Пользователь не авторизован");
        }
    }
}