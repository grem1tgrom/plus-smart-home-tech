package ru.yandex.practicum.service.handler;

public interface EventService<T> {

    void send(T event);
}
