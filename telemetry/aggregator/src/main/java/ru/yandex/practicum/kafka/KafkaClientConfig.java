package ru.yandex.practicum.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.config.KafkaClientProperties;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(KafkaClientProperties.class)
public class KafkaClientConfig {

    @Bean
    public Properties producerProperties(KafkaClientProperties props) {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers());
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, props.getProducer().getKeySerializer());
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, props.getProducer().getValueSerializer());
        p.put(ProducerConfig.CLIENT_ID_CONFIG, props.getProducer().getClientId());
        return p;
    }

    @Bean
    public Producer<String, SpecificRecordBase> producer(Properties producerProperties) {
        return new KafkaProducer<>(producerProperties);
    }

    @Bean
    public Properties consumerProperties(KafkaClientProperties props) {
        Properties c = new Properties();
        c.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers());
        c.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, props.getConsumer().getKeyDeserializer());
        c.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, props.getConsumer().getValueDeserializer());
        c.put(ConsumerConfig.GROUP_ID_CONFIG, props.getConsumer().getGroupId());
        c.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(props.getConsumer().getEnableAutoCommit()));
        c.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, props.getConsumer().getAutoOffsetReset());
        return c;
    }

    @Bean
    public Consumer<String, SpecificRecordBase> consumer(Properties consumerProperties) {
        return new KafkaConsumer<>(consumerProperties);
    }

    @Bean
    public KafkaClient kafkaClient(Producer<String, SpecificRecordBase> producer,
                                   Consumer<String, SpecificRecordBase> consumer) {
        return new KafkaClientImpl(producer, consumer);
    }
}