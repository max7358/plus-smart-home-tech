package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final Producer<String, SensorsSnapshotAvro> producer;
    private final Consumer<String, SensorEventAvro> consumer;
    private final String topicSensors;
    private final String topicSnapshots;
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Autowired
    AggregationStarter(KafkaProperties kafkaProperties) {
        this.producer = kafkaProperties.kafkaProducer();
        this.consumer = kafkaProperties.kafkaConsumer();
        this.topicSnapshots = kafkaProperties.getTopicSnapshots();
        this.topicSensors = kafkaProperties.getTopicSensors();
    }

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(topicSensors));

            // Цикл обработки событий
            while (true) {
                ConsumerRecords<String, SensorEventAvro> consumerRecords = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, SensorEventAvro> consumerRecord : consumerRecords) {
                    log.debug("Processing record with key: {}, value: {}", consumerRecord.key(), consumerRecord.value());
                    updateState(consumerRecord.value()).ifPresent(snapshot -> {
                        try {
                            producer.send(new ProducerRecord<>(topicSnapshots, snapshot.getHubId(), snapshot));
                        } catch (Exception e) {
                            log.error("Message send failed: {}", e.getMessage());
                        }
                    });
                }
            }

        } catch (WakeupException ignored) {
            log.info("Shutdown signal received. Exiting...");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедиться,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы

                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                producer.flush();
                // здесь нужно вызвать метод консьюмера для фиксиции смещений
                consumer.commitSync();
            } catch (Exception e) {
                log.error("Error during closing resources", e);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubId(), new SensorsSnapshotAvro());

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());

        if (oldState != null) {
            if (oldState.getTimestamp().isBefore(Instant.ofEpochSecond(event.getTimestamp()))
                    && oldState.getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }

        // Обновляем снапшот новыми данными
        SensorStateAvro newState = new SensorStateAvro(Instant.ofEpochSecond(event.getTimestamp()), event.getPayload());
        snapshot.getSensorsState().put(event.getId(), newState);
        snapshot.setTimestamp(Instant.ofEpochSecond(event.getTimestamp()));

        // Сохраняем обновленный снапшот
        snapshot.put(event.getHubId(), snapshot);
        snapshots.put(event.getHubId(), snapshot);
        return Optional.of(snapshot);
    }
}