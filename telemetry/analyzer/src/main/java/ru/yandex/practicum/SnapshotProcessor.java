package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class SnapshotProcessor {
    private final Consumer<String, SensorsSnapshotAvro> consumer;
    private final String topicSnapshots;

    @Autowired
    SnapshotProcessor(KafkaProperties kafkaProperties) {
        this.consumer = kafkaProperties.kafkaSnapshotConsumer();
        this.topicSnapshots = kafkaProperties.getTopicSnapshots();
    }


    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(topicSnapshots));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> consumerRecords = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, SensorsSnapshotAvro> consumerRecord : consumerRecords) {

                }
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий", e);
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
