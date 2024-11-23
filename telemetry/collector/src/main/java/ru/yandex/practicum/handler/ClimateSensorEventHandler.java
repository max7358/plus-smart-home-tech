package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends AbstractSensorEventHandler<ClimateSensorAvro> {

    ClimateSensorEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro toAvro(SensorEventProto event) {
        ClimateSensorEvent sensorEvent = event.getClimateSensorEvent();
        return ClimateSensorAvro.newBuilder().setCo2Level(sensorEvent.getCo2Level())
                .setHumidity(sensorEvent.getHumidity()).setTemperatureC(sensorEvent.getTemperatureC()).build();
    }
}
