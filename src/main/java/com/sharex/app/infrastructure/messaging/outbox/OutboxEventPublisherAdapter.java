package com.sharex.app.infrastructure.messaging.outbox;

import com.sharex.app.infrastructure.persistence.outbox.OutboxEventEntity;
import com.sharex.app.infrastructure.persistence.outbox.OutboxEventRepository;
import com.sharex.app.shared.event.DomainEvent;
import com.sharex.app.shared.event.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxEventPublisherAdapter implements EventPublisher {

    private final ObjectMapper mapper;
    private final OutboxEventRepository repo;

    public OutboxEventPublisherAdapter(ObjectMapper mapper, OutboxEventRepository repo) {
        this.mapper = mapper;
        this.repo = repo;
    }

    @Override
    @Transactional
    public void publish(DomainEvent event) {

        try {
            OutboxEventEntity record = new OutboxEventEntity();

            // 1) Determine eventType (topic)
            String eventType;
            try {
                var m = event.getClass().getMethod("eventType");
                eventType = (String) m.invoke(event);
            } catch (Exception e) {
                eventType = event.getClass().getSimpleName();
            }

            // 2) Determine aggregateType
            String aggregateType = event.getClass().getSimpleName().toLowerCase();
            if (aggregateType.contains("event"))
                aggregateType = aggregateType.replace("event", "");

            // 3) Save JSON payload
            record.setAggregateType(aggregateType);
            record.setEventType(eventType);
            record.setPayload(mapper.writeValueAsString(event));

            repo.save(record);

            System.out.println("[OUTBOX] Stored event in outbox: " + eventType);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to persist event in Outbox", ex);
        }
    }
}
