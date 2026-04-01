package ru.yandex.practicum.service.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SensorEventKafkaPublisher {

    private final KafkaClient kafkaClient;

    @Value("${topics.sensor-event}")
    private String topic;

    public void publish(SensorEventAvro event) {
        String hubId = event.getHubId().toString();
        Instant ts = event.getTimestamp() != null ? event.getTimestamp() : Instant.now();

        kafkaClient.send(topic, hubId, ts, event);
    }
}