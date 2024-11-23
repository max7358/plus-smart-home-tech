package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends AbstractSensorEventHandler<SwitchSensorAvro> {

    SwitchSensorEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    protected SwitchSensorAvro toAvro(SensorEventProto event) {
        SwitchSensorEvent sensorEvent = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder().setState(sensorEvent.getState()).build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }
}
