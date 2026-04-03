package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class ShoppingCartController implements ShoppingCartClient {

    private final CartService cartService;

    @Override
    @GetMapping
    public ShoppingCartDto getCart(@RequestParam @NotBlank String username) {
        checkUser(username);
        return cartService.getCart(username);
    }

    @Override
    @PutMapping
    public ShoppingCartDto addProductToCart(
            @RequestParam @NotBlank String username,
            @RequestBody
            @Valid
            @NotNull
            @NotEmpty
            Map<@NotNull UUID, @NotNull @Positive Long> products
    ) {
        checkUser(username);
        return cartService.addProductToCart(username, products);
    }

    @Override
    @DeleteMapping
    public void deleteCart(@RequestParam @NotBlank String username) {
        checkUser(username);
        cartService.deleteCart(username);
    }

    @Override
    @PostMapping("/remove")
    public ShoppingCartDto removeFromCart(
            @RequestParam @NotBlank String username,
            @RequestBody
            @Valid
            @NotNull
            Set<@NotNull UUID> productIds
    ) {
        checkUser(username);
        return cartService.removeFromCart(username, productIds);
    }

    @Override
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody @Valid @NotNull ChangeProductQuantityRequest request
    ) {
        checkUser(username);
        return cartService.changeProductQuantity(username, request);
    }

    private void checkUser(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Пользователь не авторизован");
        }
    }
}