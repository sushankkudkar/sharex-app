package com.sharex.app.module.usergroup.domain.event;

import com.sharex.app.shared.event.DomainEvent;

import java.time.Instant;

public record UserCreatedEvent(
        String eventId,
        String eventType,       // e.g. "user.events.user.created.v1"
        Integer eventVersion,   // e.g. 1
        String schemaVersion,   // e.g. "1.0.0"
        String userId,
        String name,
        String email,
        Instant occurredAt
) implements DomainEvent { }
