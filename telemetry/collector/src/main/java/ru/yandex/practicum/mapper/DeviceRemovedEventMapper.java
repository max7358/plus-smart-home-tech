package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.model.DeviceRemovedEvent;
import ru.yandex.practicum.model.HubEvent;

@UtilityClass
public class DeviceRemovedEventMapper {
    public DeviceRemovedEventAvro toAvro(HubEvent hubEvent) {
        DeviceRemovedEvent event = (DeviceRemovedEvent) hubEvent;
        return new DeviceRemovedEventAvro(event.getId());
    }
}
