package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ScenarioActionRepository;
import ru.yandex.practicum.repository.ScenarioConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;

@Service
public class SnapshotService {
    private final ScenarioRepository scenarioRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final HubRouterClient hubRouterClient;
    private final ScenarioActionRepository scenarioActionRepository;

    public SnapshotService(ScenarioRepository scenarioRepository,
                           ScenarioConditionRepository scenarioConditionRepository, HubRouterClient hubRouterClient1, ScenarioActionRepository scenarioActionRepository) {
        this.scenarioRepository = scenarioRepository;
        this.scenarioConditionRepository = scenarioConditionRepository;
        this.hubRouterClient = hubRouterClient1;
        this.scenarioActionRepository = scenarioActionRepository;
    }

    public void analyze(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        for (Scenario scenario : scenarios) {
            if (checkScenario(scenario, snapshot)) {
                runActions(scenario.getActions(), hubId);
            }
        }

    }

    private boolean checkScenario(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (Condition condition : scenario.getConditions()) {
            if (!checkCondition(condition, snapshot))
                return false;
        }
        return true;
    }

    private boolean checkCondition(Condition condition, SensorsSnapshotAvro snapshot) {
        ScenarioCondition scenarioCondition = scenarioConditionRepository.findByConditionId(condition.getId());
        SensorStateAvro sensorState = snapshot.getSensorsState().get(scenarioCondition.getSensor().getId());

        switch (condition.getType()) {
            case TEMPERATURE -> {
                TemperatureSensorAvro sensor = (TemperatureSensorAvro) sensorState.getData();
                return validateCondition(sensor.getTemperatureC(), condition.getOperation(), condition.getValue());
            }
            case MOTION -> {
                MotionSensorAvro sensor = (MotionSensorAvro) sensorState.getData();
                return validateCondition(sensor.getMotion() ? 1 : 0, condition.getOperation(), condition.getValue());
            }
            case LUMINOSITY -> {
                LightSensorAvro sensor = (LightSensorAvro) sensorState.getData();
                return validateCondition(sensor.getLuminosity(), condition.getOperation(), condition.getValue());
            }
            case HUMIDITY -> {
                ClimateSensorAvro sensor = (ClimateSensorAvro) sensorState.getData();
                return validateCondition(sensor.getHumidity(), condition.getOperation(), condition.getValue());
            }
            case SWITCH -> {
                SwitchSensorAvro sensor = (SwitchSensorAvro) sensorState.getData();
                return validateCondition(sensor.getState() ? 1 : 0, condition.getOperation(), condition.getValue());
            }
            case CO2LEVEL -> {
                ClimateSensorAvro sensor = (ClimateSensorAvro) sensorState.getData();
                return validateCondition(sensor.getCo2Level(), condition.getOperation(), condition.getValue());
            }
            default -> throw new IllegalStateException("Unexpected value: " + condition.getType());
        }
    }

    private boolean validateCondition(int sensorValue, ConditionOperation operation, int conditionValue) {
        return switch (operation) {
            case GREATER_THAN -> sensorValue > conditionValue;
            case LOWER_THAN -> sensorValue < conditionValue;
            case EQUALS -> sensorValue == conditionValue;
        };
    }

    private void runActions(List<Action> actions, String hubId) {
        for (Action action : actions) {
            ScenarioAction scenarioAction = scenarioActionRepository.findByActionId(action.getId());
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setAction(DeviceActionProto.newBuilder()
                            .setSensorId(scenarioAction.getSensor().getId())
                            .setType(ActionTypeProto.valueOf(action.getType()))
                            .setValue(action.getValue())
                            .build())
                    .setHubId(hubId)
                    .setScenarioName(scenarioAction.getScenario().getName())
                    .build();
            hubRouterClient.runAction(request);
        }
    }
}
