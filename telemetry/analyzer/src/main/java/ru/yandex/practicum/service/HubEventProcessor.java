package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.mapper.ScenarioMapper;
import ru.yandex.practicum.model.Sensor;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final Consumer<String, HubEventAvro> consumer;
    private final String topicHubs;
    private final ScenarioService scenarioService;
    private final SensorService sensorService;

    @Autowired
    HubEventProcessor(KafkaProperties kafkaProperties, SensorService sensorService, ScenarioService scenarioService) {
        this.consumer = kafkaProperties.kafkaHubConsumer();
        this.topicHubs = kafkaProperties.getTopicHubs();
        this.sensorService = sensorService;
        this.scenarioService = scenarioService;
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(topicHubs));

            while (true) {
                ConsumerRecords<String, HubEventAvro> consumerRecords = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, HubEventAvro> consumerRecord : consumerRecords) {
                    HubEventAvro hubEventAvro = consumerRecord.value();

                    switch (hubEventAvro.getPayload()) {
                        case DeviceAddedEventAvro deviceAddedEventAvro -> {
                            Sensor sensor = Sensor.builder()
                                    .hubId(hubEventAvro.getHubId()).id(deviceAddedEventAvro.getId()).build();
                            sensorService.addSensor(sensor);
                        }
                        case DeviceRemovedEventAvro deviceRemovedEventAvro ->
                                sensorService.deleteSensor(hubEventAvro.getHubId(), deviceRemovedEventAvro.getId());
                        case ScenarioAddedEventAvro scenarioAddedEventAvro ->
                                scenarioService.addScenario(ScenarioMapper.fromAvro(hubEventAvro.getHubId(), scenarioAddedEventAvro));
                        case ScenarioRemovedEventAvro scenarioRemovedEventAvro ->
                                scenarioService.deleteScenario(hubEventAvro.getHubId(), scenarioRemovedEventAvro.getName());
                        default -> throw new IllegalStateException("Unexpected value: " + hubEventAvro.getPayload());
                    }

                }
            }
        } catch (WakeupException ignored) {
            log.info("WakeupException caught, shutting down...");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                consumer.commitSync();

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }
}
