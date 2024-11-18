package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.SensorEvent;
import ru.yandex.practicum.model.TemperatureSensorEvent;

@UtilityClass
public class TemperatureSensorEventMapper {
    public TemperatureSensorAvro toAvro(SensorEvent sensorEvent) {
        TemperatureSensorEvent event = (TemperatureSensorEvent) sensorEvent;
        return new TemperatureSensorAvro(event.getId(), event.getHubId(), event.getTimestamp().toEpochMilli(),
                event.getTemperatureC(), event.getTemperatureF());
    }
}
