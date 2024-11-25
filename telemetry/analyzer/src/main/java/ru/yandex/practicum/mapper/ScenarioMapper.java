package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.*;

import java.util.List;

@UtilityClass
public class ScenarioMapper {
    public Scenario fromAvro(String hubId, ScenarioAddedEventAvro event) {
        List<Condition> conditions = event.getConditions().stream()
                .map(ScenarioMapper::fromAvro)
                .toList();

        List<Action> actions = event.getActions().stream()
                .map(ScenarioMapper::fromAvro)
                .toList();

        return Scenario.builder().hubId(hubId).name(event.getName()).conditions(conditions).actions(actions).build();
    }

    public Condition fromAvro(ScenarioConditionAvro scenarioConditionAvro) {

        return Condition.builder()
                .type(ConditionType.valueOf(scenarioConditionAvro.getType().name()))
                .operation(ConditionOperation.valueOf(scenarioConditionAvro.getOperation().name()))
                .value(toInteger(scenarioConditionAvro.getValue()))
                .build();
    }

    public Action fromAvro(DeviceActionAvro actionAvro) {
        return Action.builder()
                .type(ActionType.valueOf(actionAvro.getType().name()).name())
                .value(actionAvro.getValue())
                .build();
    }

    private Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? 1 : 0;
        } else {
            return null;
        }
    }
}
