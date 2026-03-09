package ru.yandex.practicum.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaClientProperties {
    private String bootstrapServers;

    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();

    @Data
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
        private String clientId = "telemetry.aggregator";
    }

    @Data
    public static class Consumer {
        private String keyDeserializer;
        private String valueDeserializer;
        private String groupId;
        private Boolean enableAutoCommit = false;
        private String autoOffsetReset = "earliest";
    }
}