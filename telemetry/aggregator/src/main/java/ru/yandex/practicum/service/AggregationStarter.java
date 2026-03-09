package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final KafkaClient kafkaClient;
    private final SensorSnapshotService snapshotService;

    @Value("${topics.sensor-event}")
    String sensorTopic;

    @Value("${topics.snapshots}")
    String snapshotTopic;

    public void start() {
        log.info("Старт");

        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer();
        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook: wakeup consumer");
            consumer.wakeup();
        }));

        try {
            log.info("Подписка на топик: {}", sensorTopic);
            consumer.subscribe(List.of(sensorTopic));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(5));
                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    SensorEventAvro event = (SensorEventAvro) record.value();

                    snapshotService.updateState(event)
                            .ifPresent(snapshot -> {
                                ProducerRecord<String, SpecificRecordBase> recordToSend =
                                        new ProducerRecord<>(
                                                snapshotTopic,
                                                null,
                                                snapshot.getTimestamp() != null
                                                        ? snapshot.getTimestamp().toEpochMilli()
                                                        : null,
                                                snapshot.getHubId(),
                                                snapshot
                                        );

                                producer.send(recordToSend);
                            });
                }

                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
            log.info("Получено WakeupException, завершаем работу");
        } finally {
            try {
                consumer.commitSync();
            } catch (Exception e) {
                log.warn("Не удалось выполнить commitSync при остановке", e);
            }

            try {
                producer.flush();
            } catch (Exception e) {
                log.warn("Не удалось flush producer при остановке", e);
            }

            log.info("Закрываем consumer");
            consumer.close();

            log.info("Закрываем producer");
            producer.close();
        }
    }
}