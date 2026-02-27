package ru.yandex.practicum.kafka;

import org.apache.avro.specific.SpecificRecordBase;

import java.time.Instant;

public interface KafkaClient {
    void send(String topic, String key, Instant timestamp, SpecificRecordBase event);
}