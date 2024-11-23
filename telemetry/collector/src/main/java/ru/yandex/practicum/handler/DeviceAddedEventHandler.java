package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventHandler extends AbstractHubEventHandler<DeviceAddedEventAvro> {

    DeviceAddedEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro toAvro(HubEventProto event) {
        DeviceAddedEventProto hubEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder().setId(hubEvent.getId()).setType(DeviceTypeAvro.valueOf(hubEvent.getType().name())).build();
    }
}
