package ru.yandex.practicum.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

import java.time.Duration;

@RequiredArgsConstructor
public class KafkaClientImpl implements KafkaClient {

    private final Producer<String, SpecificRecordBase> producer;
    private final Consumer<String, SpecificRecordBase> consumer;

    @Override
    public Producer<String, SpecificRecordBase> getProducer() {
        return producer;
    }

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer() {
        return consumer;
    }

    @Override
    public void close() {
        try {
            if (consumer != null) consumer.close(Duration.ofSeconds(10));
        } finally {
            if (producer != null) producer.close(Duration.ofSeconds(10));
        }
    }
}