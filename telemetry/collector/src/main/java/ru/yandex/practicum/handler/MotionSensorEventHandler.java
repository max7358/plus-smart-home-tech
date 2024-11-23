package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends AbstractSensorEventHandler<MotionSensorAvro> {

    MotionSensorEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    protected MotionSensorAvro toAvro(SensorEventProto event) {
        MotionSensorEvent sensorEvent = event.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder().setLinkQuality(sensorEvent.getLinkQuality())
                .setVoltage(sensorEvent.getVoltage()).setMotion(sensorEvent.getMotion()).build();
    }
}
