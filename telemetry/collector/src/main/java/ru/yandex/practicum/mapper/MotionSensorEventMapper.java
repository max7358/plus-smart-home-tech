package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.model.MotionSensorEvent;
import ru.yandex.practicum.model.SensorEvent;

@UtilityClass
public class MotionSensorEventMapper {
    public MotionSensorAvro toAvro(SensorEvent sensorEvent) {
        MotionSensorEvent event = (MotionSensorEvent) sensorEvent;
        return new MotionSensorAvro(event.getLinkQuality(), event.isMotion(), event.getVoltage());
    }
}
