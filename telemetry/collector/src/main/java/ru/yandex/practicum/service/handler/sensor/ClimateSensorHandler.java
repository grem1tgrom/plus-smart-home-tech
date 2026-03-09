package ru.yandex.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.service.publisher.SensorEventKafkaPublisher;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClimateSensorHandler implements SensorEventHandler {

    private final SensorEventKafkaPublisher publisher;
    private final SensorEventMapper sensorEventMapper;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        publisher.publish(sensorEventMapper.toAvro(eventProto));
    }
}