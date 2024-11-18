package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.model.LightSensorEvent;
import ru.yandex.practicum.model.SensorEvent;

@UtilityClass
public class LightSensorEventMapper {
    public LightSensorAvro toAvro(SensorEvent sensorEvent) {
        LightSensorEvent event = (LightSensorEvent) sensorEvent;
        return new LightSensorAvro(event.getLinkQuality(), event.getLuminosity());
    }
}
