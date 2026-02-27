package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.hub.*;
import ru.yandex.practicum.service.EventService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubEventService implements EventService<HubEvent> {
    private final KafkaClient kafkaClient;

    @Value("${topics.hub-event}")
    private String topic;

    @Override
    public void send(HubEvent event) {
        HubEventAvro hubEventAvro = toAvro(event);
        kafkaClient.send(topic, event.getHubId(), event.getTimestamp(), hubEventAvro);
        log.info("Ивент: {}, отправлен в топик: {}", event, topic);
    }

    private HubEventAvro toAvro(HubEvent event) {
        Object payload = switch (event.getType()) {
            case DEVICE_ADDED -> toDeviceAddedAvro((DeviceAddedEvent) event);
            case DEVICE_REMOVED -> toDeviceRemovedAvro((DeviceRemovedEvent) event);
            case SCENARIO_ADDED -> toScenarioAddedAvro((ScenarioAddedEvent) event);
            case SCENARIO_REMOVED -> toScenarioRemovedAvro((ScenarioRemovedEvent) event);
        };

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private DeviceAddedEventAvro toDeviceAddedAvro(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();
    }

    private DeviceRemovedEventAvro toDeviceRemovedAvro(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    private ScenarioAddedEventAvro toScenarioAddedAvro(ScenarioAddedEvent event) {
        List<DeviceActionAvro> deviceActionAvroList = event.getActions().stream()
                .map(this::toDeviceActionAvro)
                .toList();
        List<ScenarioConditionAvro> scenarioConditionAvroList = event.getConditions().stream()
                .map(this::toScenarioConditionAvro)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(deviceActionAvroList)
                .setConditions(scenarioConditionAvroList)
                .build();
    }

    private ScenarioRemovedEventAvro toScenarioRemovedAvro(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    private DeviceActionAvro toDeviceActionAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .build();
    }

    private ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setValue(condition.getValue())
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .build();
    }
}