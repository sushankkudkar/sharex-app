package com.sharex.app.infrastructure.messaging.outbox;

import com.sharex.app.infrastructure.persistence.outbox.OutboxEventEntity;
import com.sharex.app.infrastructure.persistence.outbox.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxEventDispatcher {

    private final OutboxEventRepository repo;
    private final KafkaTemplate<String, String> kafka;

    public OutboxEventDispatcher(OutboxEventRepository repo, KafkaTemplate<String, String> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }

    // run every 5 seconds (adjust as required)
    @Scheduled(fixedDelay = 5000)
    @Transactional("writeTransactionManager")
    public void processOutbox() {
        List<OutboxEventEntity> events = repo.findByPublishedFalse();
        for (OutboxEventEntity ev : events) {
            try {
                // Use the eventType directly as topic: you should produce event.eventType = "user.events.user.created.v1"
                String topic = ev.getEventType();
                kafka.send(topic, ev.getPayload());
                ev.setPublished(true);
                repo.save(ev);
                System.out.println("[OUTBOX-DISPATCHER] Published to topic=" + topic + " outboxId=" + ev.getId());
            } catch (Exception ex) {
                // on failure, keep published=false so it is retried
                System.err.println("[OUTBOX-DISPATCHER] Failed to publish outboxId=" + ev.getId() + " - " + ex.getMessage());
            }
        }
    }
}
