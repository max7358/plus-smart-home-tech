package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.HubEvent;
import ru.yandex.practicum.model.ScenarioAddedEvent;

import java.util.List;

@UtilityClass
public class ScenarioAddedEventMapper {
    public ScenarioAddedEventAvro toAvro(HubEvent hubEvent) {
        ScenarioAddedEvent event = (ScenarioAddedEvent) hubEvent;
        List<ScenarioConditionAvro> conditions = event.getConditions().stream()
                .map(c -> new ScenarioConditionAvro(c.getSensorId(), ConditionTypeAvro.valueOf(c.getType().name()),
                        ConditionOperationAvro.valueOf(c.getOperation().name()), c.getValue())).toList();
        List<DeviceActionAvro> actions = event.getActions().stream()
                .map(a -> new DeviceActionAvro(a.getSensorId(), ActionTypeAvro.valueOf(a.getType().name()), a.getValue())).toList();
        return new ScenarioAddedEventAvro(event.getName(), conditions, actions);
    }
}
