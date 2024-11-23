package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends AbstractSensorEventHandler<LightSensorAvro> {

    LightSensorEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected LightSensorAvro toAvro(SensorEventProto event) {
        LightSensorEvent sensorEvent = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder().setLinkQuality(sensorEvent.getLinkQuality())
                .setLuminosity(sensorEvent.getLuminosity()).build();
    }
}
