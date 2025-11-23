package com.sharex.app.infrastructure.persistence.outbox;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEventEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Setter
    private String aggregateType;   // e.g. "user"
    @Setter
    private String eventType;       // e.g. "user.events.user.created.v1"

    @Setter
    @Lob
    @Column(columnDefinition = "text")
    private String payload;         // JSON payload

    @Setter
    private boolean published = false;

    private LocalDateTime createdAt;

    public OutboxEventEntity() {}

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getAggregateType() { return aggregateType; }

    public String getEventType() { return eventType; }

    public String getPayload() { return payload; }

    public boolean isPublished() { return published; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
