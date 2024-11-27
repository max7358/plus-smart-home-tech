package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends AbstractHubEventHandler<ScenarioAddedEventAvro> {

    ScenarioAddedEventHandler(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro toAvro(HubEventProto event) {
        ScenarioAddedEventProto hubEvent = event.getScenarioAdded();
        List<ScenarioConditionAvro> conditions = hubEvent.getConditionList().stream()
                .map(c -> new ScenarioConditionAvro(c.getSensorId(), ConditionTypeAvro.valueOf(c.getType().name()),
                        ConditionOperationAvro.valueOf(c.getOperation().name()), c.getType())).toList();
        List<DeviceActionAvro> actions = hubEvent.getActionList().stream()
                .map(a -> new DeviceActionAvro(a.getSensorId(), ActionTypeAvro.valueOf(a.getType().name()), a.getValue())).toList();
        return ScenarioAddedEventAvro.newBuilder().setName(hubEvent.getName()).setConditions(conditions).setActions(actions).build();
    }
}
