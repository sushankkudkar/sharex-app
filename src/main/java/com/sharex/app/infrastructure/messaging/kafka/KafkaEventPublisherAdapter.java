package com.sharex.app.infrastructure.messaging.kafka;

import com.sharex.app.shared.event.DomainEvent;
import com.sharex.app.shared.event.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisherAdapter implements EventPublisher {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;

    public KafkaEventPublisherAdapter(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        this.kafka = kafka;
        this.mapper = mapper;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String json = mapper.writeValueAsString(event);

            // derive topic name via event.eventType()
            String topic;
            try {
                var m = event.getClass().getMethod("eventType");
                topic = (String) m.invoke(event);
            } catch (Exception e) {
                topic = event.getClass().getSimpleName();
            }

            kafka.send(topic, json);
            System.out.println("[KAFKA-PUBLISHER] Sent event to topic=" + topic);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to publish event to Kafka", ex);
        }
    }
}
