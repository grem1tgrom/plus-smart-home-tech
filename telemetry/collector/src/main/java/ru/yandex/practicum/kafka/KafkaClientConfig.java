package ru.yandex.practicum.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaProducerProperties.class)
public class KafkaClientConfig {

    @Bean
    public Producer<String, SpecificRecordBase> kafkaProducer(KafkaProducerProperties props) {
        return new KafkaProducer<>(props.toKafkaProperties());
    }

    @Bean
    public KafkaClient kafkaClient(Producer<String, SpecificRecordBase> producer) {
        return new KafkaEventProducer(producer);
    }
}