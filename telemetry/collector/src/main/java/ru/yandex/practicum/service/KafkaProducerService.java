package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.mapper.*;
import ru.yandex.practicum.model.*;

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
        sendMessage(topicSensors, convertSensorEvent(event));
    }

    private void sendMessage(String topic, SpecificRecordBase recordBase) {

        kafkaProducer.send(new ProducerRecord<>(topic, recordBase));
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

    public void sendHubEvent(@Valid HubEvent event) {
        sendMessage(topicHubs, convertHubEvent(event));
    }
}
