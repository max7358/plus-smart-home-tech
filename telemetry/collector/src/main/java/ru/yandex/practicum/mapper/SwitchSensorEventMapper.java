package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.SensorEvent;
import ru.yandex.practicum.model.SwitchSensorEvent;

@UtilityClass
public class SwitchSensorEventMapper {
    public SwitchSensorAvro toAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent event = (SwitchSensorEvent) sensorEvent;
        return new SwitchSensorAvro(event.isState());
    }
}
