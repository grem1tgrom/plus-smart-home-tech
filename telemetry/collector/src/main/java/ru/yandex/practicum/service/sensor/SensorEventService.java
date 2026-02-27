package ru.yandex.practicum.service.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.sensor.*;
import ru.yandex.practicum.service.EventService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventService implements EventService<SensorEvent> {
    private final KafkaClient kafkaClient;

    @Value("${topics.sensor-event}")
    private String topic;

    @Override
    public void send(SensorEvent event) {
        SensorEventAvro sensorEventAvro = toAvro(event);
        kafkaClient.send(topic, event.getHubId(), event.getTimestamp(), sensorEventAvro);
        log.info("Ивент: {}, отправлен в топик: {}", event, topic);
    }

    private SensorEventAvro toAvro(SensorEvent event) {
        Object payload = switch (event.getType()) {
            case CLIMATE_SENSOR_EVENT -> toClimateSensorAvro((ClimateSensorEvent) event);
            case LIGHT_SENSOR_EVENT -> toLightSensorAvro((LightSensorEvent) event);
            case MOTION_SENSOR_EVENT -> toMotionSensorAvro((MotionSensorEvent) event);
            case SWITCH_SENSOR_EVENT -> toSwitchSensorAvro((SwitchSensorEvent) event);
            case TEMPERATURE_SENSOR_EVENT -> toTemperatureSensorAvro((TemperatureSensorEvent) event);
        };

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private ClimateSensorAvro toClimateSensorAvro(ClimateSensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(event.getCo2Level())
                .setHumidity(event.getHumidity())
                .setTemperatureC(event.getTemperatureC())
                .build();
    }

    private LightSensorAvro toLightSensorAvro(LightSensorEvent event) {
        return LightSensorAvro.newBuilder()
                .setLuminosity(event.getLuminosity())
                .setLinkQuality(event.getLinkQuality())
                .build();
    }

    private MotionSensorAvro toMotionSensorAvro(MotionSensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setMotion(event.getMotion())
                .setLinkQuality(event.getLinkQuality())
                .setVoltage(event.getVoltage())
                .build();
    }

    private SwitchSensorAvro toSwitchSensorAvro(SwitchSensorEvent event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    private TemperatureSensorAvro toTemperatureSensorAvro(TemperatureSensorEvent event) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}