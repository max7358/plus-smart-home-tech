package ru.yandex.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.concurrent.Future;

@Slf4j
public abstract class AbstractSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    private final Producer<String, SpecificRecordBase> kafkaProducer;
    private final String topicSensors;

    @Autowired
    AbstractSensorEventHandler(KafkaProperties kafkaProperties) {
        this.kafkaProducer = kafkaProperties.kafkaProducer();
        this.topicSensors = kafkaProperties.getTopicSensors();
    }

    @Override
    public void handle(SensorEventProto event) {
        T recordBase = toAvro(event);
        try {
            Future<RecordMetadata> send = kafkaProducer.send(new ProducerRecord<>(topicSensors, event.getId(), recordBase));
            if (send.isDone()) {
                send.resultNow();
            }
        } catch (SerializationException e) {
            log.error("Message serialization failed: {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Message send failed: {}", e.getMessage());
        }
    }

    protected abstract T toAvro(SensorEventProto event);
}
