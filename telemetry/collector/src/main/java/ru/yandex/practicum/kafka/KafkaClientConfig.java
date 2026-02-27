package ru.yandex.practicum.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaClientConfig {

    @Bean
    public Producer<String, SpecificRecordBase> kafkaProducer(
            @Value("${kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${kafka.producer.key-serializer}") String keySerializer,
            @Value("${kafka.producer.value-serializer}") String valueSerializer,
            @Value("${kafka.producer.properties.client.id:telemetry.collector}") String clientId
    ) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return new KafkaProducer<>(config);
    }

    @Bean
    public KafkaClient kafkaClient(Producer<String, SpecificRecordBase> producer) {
        return new KafkaClientImpl(producer);
    }
}