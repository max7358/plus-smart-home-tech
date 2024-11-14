package ru.yandex.practicum.model;

public class LightSensorEvent extends SensorEvent {
    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
