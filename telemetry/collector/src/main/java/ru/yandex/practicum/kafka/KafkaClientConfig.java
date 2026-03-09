package ru.yandex.practicum.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Configuration
public class KafkaClientConfig {

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {
            @Value("${kafka.bootstrap-servers}")
            private String bootstrapServers;

            @Value("${kafka.producer.key-serializer}")
            private String keySerializer;

            @Value("${kafka.producer.value-serializer}")
            private String valueSerializer;

            private Producer<String, SpecificRecordBase> producer;

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

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
                log.debug("Сохраняю событие {} с ключом {} в топик {}", eventClass, key, topic);

                Future<RecordMetadata> future = getProducer().send(record);
                getProducer().flush();

                try {
                    RecordMetadata metadata = future.get();
                    log.info("Событие {} успешно сохранено в топик {} в партицию {} со смещением {}",
                            eventClass, metadata.topic(), metadata.partition(), metadata.offset());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Не удалось записать событие {} в топик {} (interrupt)", eventClass, topic, e);
                } catch (ExecutionException e) {
                    log.warn("Не удалось записать событие {} в топик {}", eventClass, topic, e);
                }
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

                producer = new KafkaProducer<>(config);
            }

            @Override
            public void close() {
                if (producer != null) {
                    producer.flush();
                    producer.close(Duration.ofSeconds(10));
                }
            }
        };
    }
}