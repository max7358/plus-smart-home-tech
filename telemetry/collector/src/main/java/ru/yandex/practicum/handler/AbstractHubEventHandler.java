package ru.yandex.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.util.concurrent.Future;

@Slf4j
public abstract class AbstractHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    private final Producer<String, SpecificRecordBase> kafkaProducer;
    private final String topicHubs;

    @Autowired
    AbstractHubEventHandler(KafkaProperties kafkaProperties) {
        this.kafkaProducer = kafkaProperties.kafkaProducer();
        this.topicHubs = kafkaProperties.getTopicHubs();
    }

    @Override
    public void handle(HubEventProto event) {
        T recordBase = toAvro(event);
        try {
            Future<RecordMetadata> send = kafkaProducer.send(new ProducerRecord<>(topicHubs, event.getHubId(), recordBase));
            if (send.isDone()) {
                send.resultNow();
            }
        } catch (SerializationException e) {
            log.error("Message serialization failed: {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Message send failed: {}", e.getMessage());
        }
    }

    protected abstract T toAvro(HubEventProto event);
}
