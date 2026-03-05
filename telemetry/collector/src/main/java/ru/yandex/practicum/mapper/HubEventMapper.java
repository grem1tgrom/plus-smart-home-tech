package ru.yandex.practicum.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class HubEventMapper {
    public HubEventAvro toAvro(HubEventProto event) {
        log.info("Определение формата ивента");

        if (event.getPayloadCase() == HubEventProto.PayloadCase.PAYLOAD_NOT_SET) {
            throw new IllegalArgumentException("Payload not set: " + event);
        }

        Object payload = switch (event.getPayloadCase()) {
            case DEVICE_ADDED -> toDeviceAddedAvro(event.getDeviceAdded());
            case DEVICE_REMOVED -> toDeviceRemovedAvro(event.getDeviceRemoved());
            case SCENARIO_ADDED -> toScenarioAddedAvro(event.getScenarioAdded());
            case SCENARIO_REMOVED -> toScenarioRemovedAvro(event.getScenarioRemoved());
            case PAYLOAD_NOT_SET -> throw new IllegalArgumentException("Payload not set: " + event);
        };

        Instant timestamp = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos()
        );

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();
    }

    private DeviceAddedEventAvro toDeviceAddedAvro(DeviceAddedEventProto event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getType().name()))
                .build();
    }

    private DeviceRemovedEventAvro toDeviceRemovedAvro(DeviceRemovedEventProto event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    private ScenarioAddedEventAvro toScenarioAddedAvro(ScenarioAddedEventProto event) {
        List<DeviceActionAvro> actions = event.getActionList().stream()
                .map(this::toDeviceActionAvro)
                .toList();
        List<ScenarioConditionAvro> conditions = event.getConditionList().stream()
                .map(this::toScenarioConditionAvro)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();
    }

    private ScenarioRemovedEventAvro toScenarioRemovedAvro(ScenarioRemovedEventProto event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    private DeviceActionAvro toDeviceActionAvro(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setSensorId(action.getSensorId())
                .setValue(action.hasValue() ? action.getValue() : null)
                .build();
    }

    private ScenarioConditionAvro toScenarioConditionAvro(ScenarioConditionProto condition) {
        Object value = switch (condition.getValueCase()) {
            case BOOL_VALUE -> condition.getBoolValue();
            case INT_VALUE -> condition.getIntValue();
            case VALUE_NOT_SET -> null;
        };
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setValue(value)
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .build();
    }
}
