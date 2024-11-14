package ru.yandex.practicum.model;

public class SwitchSensorEvent extends SensorEvent {
    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
