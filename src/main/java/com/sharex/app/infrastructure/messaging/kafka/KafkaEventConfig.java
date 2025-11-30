package com.sharex.app.infrastructure.messaging.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaEventConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProperties properties) {

        Map<String, Object> props = properties.buildProducerProperties();

        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Ensure prefix exists
        String txPrefix = properties.getProducer().getTransactionIdPrefix();
        if (txPrefix == null) {
            throw new IllegalStateException(
                    "You must set spring.kafka.producer.transaction-id-prefix in application.yml"
            );
        }

        // Kafka uses <prefix>-<random> internally for each producer instance
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, txPrefix + UUID.randomUUID());

        DefaultKafkaProducerFactory<String, String> factory =
                new DefaultKafkaProducerFactory<>(props);

        factory.setTransactionIdPrefix(txPrefix);

        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
            ProducerFactory<String, String> producerFactory) {

        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
        return template;
    }
}
