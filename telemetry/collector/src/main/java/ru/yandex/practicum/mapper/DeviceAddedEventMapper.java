package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.model.DeviceAddedEvent;
import ru.yandex.practicum.model.HubEvent;

@UtilityClass
public class DeviceAddedEventMapper {
    public DeviceAddedEventAvro toAvro(HubEvent hubEvent) {
        DeviceAddedEvent event = (DeviceAddedEvent) hubEvent;
        return new DeviceAddedEventAvro(event.getId(), DeviceTypeAvro.valueOf(event.getDeviceType().name()));
    }
}