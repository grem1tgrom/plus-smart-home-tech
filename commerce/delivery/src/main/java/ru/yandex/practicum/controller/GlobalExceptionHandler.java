package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.*;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NoDeliveryFoundException.class,
            NoOrderFoundException.class,
            NoPaymentFoundException.class,
            ProductNotFoundException.class,
            NoSpecifiedProductInWarehouseException.class,
            NoBookingFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundExceptions(RuntimeException ex) {
        log.warn("Ресурс не найден: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler({
            SpecifiedProductAlreadyInWarehouseException.class,
            NotEnoughInfoInOrderToCalculateException.class,
            NoProductsInShoppingCartException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequestExceptions(RuntimeException ex) {
        log.warn("Ошибка бизнес-логики: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleAllUncaughtException(Exception ex) {
        log.error("Внутренняя ошибка сервера", ex);
        return Map.of("error", "Произошла непредвиденная ошибка: " + ex.getMessage());
    }
}