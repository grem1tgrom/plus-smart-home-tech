package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
public class ShoppingCartController implements ShoppingCartClient {

    private final CartService cartService;

    @Override
    @GetMapping
    public ShoppingCartDto getCart(@RequestParam String username) {
        checkUser(username);
        return cartService.getCart(username);
    }

    @Override
    @PutMapping
    public ShoppingCartDto addProductToCart(
            @RequestParam String username,
            @RequestBody Map<UUID, Long> products
    ) {
        checkUser(username);
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Список товаров не должен быть пустым");
        }
        return cartService.addProductToCart(username, products);
    }

    @Override
    @DeleteMapping
    public void deleteCart(@RequestParam String username) {
        checkUser(username);
        cartService.deleteCart(username);
    }

    @Override
    @PostMapping("/remove")
    public ShoppingCartDto removeFromCart(
            @RequestParam String username,
            @RequestBody(required = false) Set<UUID> productIds
    ) {
        checkUser(username);
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Список productIds не должен быть пустым");
        }
        return cartService.removeFromCart(username, productIds);
    }

    @Override
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(
            @RequestParam String username,
            @RequestBody(required = false) ChangeProductQuantityRequest request
    ) {
        checkUser(username);
        if (request == null) {
            throw new IllegalArgumentException("Тело запроса не должно быть пустым");
        }
        return cartService.changeProductQuantity(username, request);
    }

    private void checkUser(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Пользователь не авторизован");
        }
    }
}