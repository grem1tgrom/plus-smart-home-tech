package ru.yandex.practicum.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class KafkaEventProducer implements KafkaClient {

    private final Producer<String, SpecificRecordBase> producer;

    @Override
    public void send(String topic, String key, Instant timestamp, SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp != null ? timestamp.toEpochMilli() : null,
                key,
                event
        );

        String eventClass = event != null ? event.getClass().getSimpleName() : "null";
        log.debug("Сохраняю событие {} связанное с хабом {} в топик {}", eventClass, key, topic);

        Future<RecordMetadata> future = producer.send(record);
        producer.flush();

        try {
            RecordMetadata meta = future.get();
            log.info("Событие {} было успешно сохранёно в топик {} в партицию {} со смещением {}",
                    eventClass, meta.topic(), meta.partition(), meta.offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Не удалось записать событие {} в топик {} (interrupt)", eventClass, topic, e);
        } catch (ExecutionException e) {
            log.warn("Не удалось записать событие {} в топик {}", eventClass, topic, e);
        }
    }

    @Override
    public Producer<String, SpecificRecordBase> getProducer() {
        return producer;
    }

    @Override
    public void close() {
        try {
            producer.flush();
        } finally {
            producer.close(Duration.ofSeconds(10));
        }
    }
}