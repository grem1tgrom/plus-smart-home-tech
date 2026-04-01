package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ShoppingCartDeactivatedException extends RuntimeException {
    public ShoppingCartDeactivatedException(String message) {
        super(message);
    }
}