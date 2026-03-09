package ru.yandex.practicum.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorEventKafkaPublisher {

    private final KafkaClient kafkaClient;

    @Value("${topics.sensor-event}")
    private String topic;

    public void publish(SensorEventAvro event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                event.getTimestamp() != null ? event.getTimestamp().toEpochMilli() : null,
                event.getHubId(),
                event
        );

        kafkaClient.getProducer().send(record);
        log.info("Ивент: {} отправлен в топик: {}", event, topic);
    }
}