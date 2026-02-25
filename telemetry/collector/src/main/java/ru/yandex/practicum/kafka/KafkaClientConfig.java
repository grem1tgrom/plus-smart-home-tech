package ru.yandex.practicum.kafka;

import java.util.Properties;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaClientConfig {

    @Bean
    public KafkaClient getClient() {
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
            public void stop() {
                if (producer != null) {
                    producer.close();
                }
            }

            private void initProducer() {
                final Properties config = new Properties();

                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

                producer = new KafkaProducer<>(config);
            }
        };
    }
}