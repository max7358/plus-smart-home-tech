package ru.yandex.practicum.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.kafka.consumer")
public class KafkaProperties {
    private Properties snapshotProperties;
    private Properties hubProperties;

    @Value("${spring.kafka.topic.hubs}")
    String topicHubs;

    @Value("${spring.kafka.topic.snapshots}")
    String topicSnapshots;

    @Bean
    public Consumer<String, SensorsSnapshotAvro> kafkaSnapshotConsumer() {
        return new KafkaConsumer<>(snapshotProperties);
    }

    @Bean
    public Consumer<String, HubEventAvro> kafkaHubConsumer() {
        return new KafkaConsumer<>(hubProperties);
    }
}
