package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.HubEvent;
import ru.yandex.practicum.model.ScenarioRemovedEvent;

@UtilityClass
public class ScenarioRemovedEventMapper {
    public ScenarioRemovedEventAvro toAvro(HubEvent hubEvent) {
        ScenarioRemovedEvent event = (ScenarioRemovedEvent) hubEvent;
        return new ScenarioRemovedEventAvro(event.getName());
    }
}
