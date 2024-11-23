package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler extends AbstractSensorEventHandler<TemperatureSensorAvro> {

    TemperatureSensorEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    protected TemperatureSensorAvro toAvro(SensorEventProto event) {
        TemperatureSensorEvent sensorEvent = event.getTemperatureSensorEvent();
        return TemperatureSensorAvro.newBuilder().setTemperatureC(sensorEvent.getTemperatureC())
                .setTemperatureF(sensorEvent.getTemperatureF()).build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
