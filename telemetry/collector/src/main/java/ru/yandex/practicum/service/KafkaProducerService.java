package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.mapper.*;
import ru.yandex.practicum.model.*;

import java.util.concurrent.Future;

@Slf4j
@Service
public class KafkaProducerService {

    private final Producer<String, SpecificRecordBase> kafkaProducer;
    private final String topicHubs;
    private final String topicSensors;

    @Autowired
    KafkaProducerService(KafkaProperties kafkaProperties) {
        this.kafkaProducer = kafkaProperties.kafkaProducer();
        this.topicHubs = kafkaProperties.getTopicHubs();
        this.topicSensors = kafkaProperties.getTopicSensors();
    }

    public void sendSensorEvent(SensorEvent event) {
        sendMessage(topicSensors, event.getHubId(), convertSensorEvent(event));
    }

    private void sendMessage(String topic, String id, SpecificRecordBase recordBase) {
        try {
            Future<RecordMetadata> send = kafkaProducer.send(new ProducerRecord<>(topic, id, recordBase));
            if (send.isDone()) {
                send.resultNow();
            }
        } catch (SerializationException e) {
            log.error("Message serialization failed: {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Message send failed: {}", e.getMessage());
        }
    }

    private SpecificRecordBase convertSensorEvent(SensorEvent event) {
        switch (event.getType()) {
            case LIGHT_SENSOR_EVENT -> {
                return LightSensorEventMapper.toAvro(event);
            }
            case MOTION_SENSOR_EVENT -> {
                return MotionSensorEventMapper.toAvro(event);
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                return TemperatureSensorEventMapper.toAvro(event);
            }
            case CLIMATE_SENSOR_EVENT -> {
                return ClimateSensorEventMapper.toAvro(event);
            }
            case SWITCH_SENSOR_EVENT -> {
                return SwitchSensorEventMapper.toAvro(event);
            }
            default -> throw new IllegalArgumentException("Unsupported sensor event: " + event);
        }
    }

    private SpecificRecordBase convertHubEvent(HubEvent event) {
        switch (event.getType()) {
            case DEVICE_ADDED -> {
                return DeviceAddedEventMapper.toAvro(event);
            }
            case DEVICE_REMOVED -> {
                return DeviceRemovedEventMapper.toAvro(event);
            }
            case SCENARIO_ADDED -> {
                return ScenarioAddedEventMapper.toAvro(event);
            }
            case SCENARIO_REMOVED -> {
                return ScenarioRemovedEventMapper.toAvro(event);
            }
            default -> throw new IllegalArgumentException("Unsupported hub event: " + event);
        }
    }

    public void sendHubEvent(HubEvent event) {
        sendMessage(topicHubs, event.getHubId(), convertHubEvent(event));
    }
}
