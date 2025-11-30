package com.sharex.app.module.usergroup.domain.event;

import com.sharex.app.infrastructure.messaging.outbox.domain.DomainEvent;

import java.time.Instant;

public record UserCreatedEvent(
        String eventId,
        String aggregateId,
        String name,
        String email,
        Instant occurredAt
) implements DomainEvent {

    @Override
    public String eventType() {
        return "user.events.user.created.v1";
    }

    @Override
    public int version() {
        return 1;
    }

    @Override
    public String schemaVersion() {
        return "1.0.0";
    }
}
