package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler extends AbstractHubEventHandler<DeviceRemovedEventAvro> {

    DeviceRemovedEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro toAvro(HubEventProto event) {
        DeviceAddedEventProto hubEvent = event.getDeviceAdded();
        return DeviceRemovedEventAvro.newBuilder().setId(hubEvent.getId()).build();
    }
}
