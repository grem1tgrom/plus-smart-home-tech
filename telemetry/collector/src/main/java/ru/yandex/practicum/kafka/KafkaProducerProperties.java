package ru.yandex.practicum.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerProperties {

    private String keySerializer;
    private String valueSerializer;
    private Map<String, String> properties = new HashMap<>();

    private String bootstrapServers;

    public Properties toKafkaProperties() {
        Properties p = new Properties();

        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        if (properties != null) {
            properties.forEach(p::put);
        }

        return p;
    }
}