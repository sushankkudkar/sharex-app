package com.sharex.app.infrastructure.messaging.outbox.domain;

import java.time.Instant;

public interface DomainEvent {
    String eventId();
    String aggregateId();
    String eventType();
    int version();
    String schemaVersion();
    Instant occurredAt();
}
