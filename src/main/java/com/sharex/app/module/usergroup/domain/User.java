package com.sharex.app.module.usergroup.domain;

import com.sharex.app.shared.event.DomainEvent;
import com.sharex.app.module.usergroup.domain.event.UserCreatedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private final String userId;
    private final String name;
    private final String email;

    private final List<DomainEvent> events = new ArrayList<>();

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;

        // Raise domain event with FULL metadata
        events.add(new UserCreatedEvent(
                UUID.randomUUID().toString(),      // eventId
                "user.events.user.created.v1",     // eventType (Kafka topic)
                1,                                  // eventVersion
                "1.0.0",                            // schemaVersion
                userId,                             // aggregate id
                name,
                email,
                Instant.now()                       // occurredAt
        ));
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public List<DomainEvent> getEvents() {
        return events;
    }
}
