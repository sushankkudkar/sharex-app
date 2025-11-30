package com.sharex.app.module.usergroup.domain;

import com.sharex.app.infrastructure.messaging.outbox.domain.DomainEvent;
import com.sharex.app.module.usergroup.domain.event.UserCreatedEvent;
//import com.sharex.app.infrastructure.messaging.outbox.domain.UserCreatedEvent;

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

        // Raise domain event
        raiseEvent(new UserCreatedEvent(
                UUID.randomUUID().toString(),   // eventId
                userId,                         // aggregateId
                name,
                email,
                Instant.now()                   // occurredAt
        ));
    }

    private void raiseEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> getEvents() {
        return List.copyOf(events);
    }

    public String getUserId() { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }
}
