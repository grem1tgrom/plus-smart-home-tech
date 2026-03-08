package ru.yandex.practicum.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

import java.time.Instant;

public interface KafkaClient extends AutoCloseable {

    void send(String topic, String key, Instant timestamp, SpecificRecordBase event);

    Producer<String, SpecificRecordBase> getProducer();

    @Override
    void close();
}