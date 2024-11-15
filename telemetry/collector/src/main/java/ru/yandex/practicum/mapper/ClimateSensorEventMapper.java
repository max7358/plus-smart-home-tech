package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.model.ClimateSensorEvent;
import ru.yandex.practicum.model.SensorEvent;

@UtilityClass
public class ClimateSensorEventMapper {
    public ClimateSensorAvro toAvro(SensorEvent sensorEvent) {
        ClimateSensorEvent event = (ClimateSensorEvent) sensorEvent;
        return new ClimateSensorAvro(event.getTemperatureC(), event.getHumidity(), event.getCo2Level());
    }
}
