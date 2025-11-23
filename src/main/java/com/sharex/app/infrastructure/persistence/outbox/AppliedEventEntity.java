package com.sharex.app.infrastructure.persistence.outbox;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applied_events")
public class AppliedEventEntity {

    @Id
    @Column(length = 36)
    private String eventId;  // equals event.eventId()

    private String eventType;

    private LocalDateTime appliedAt;

    public AppliedEventEntity() {}

    public AppliedEventEntity(String eventId, String eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.appliedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (appliedAt == null) appliedAt = LocalDateTime.now();
    }

    // getters/setters
    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
}
