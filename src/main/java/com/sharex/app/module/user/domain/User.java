package com.sharex.app.module.user.domain;

import com.sharex.app.infrastructure.messaging.outbox.domain.DomainEvent;
import com.sharex.app.module.user.domain.event.UserCreatedEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String email;

    private final List<DomainEvent> events = new ArrayList<>();

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;

        // Raise domain event
        raiseEvent(new UserCreatedEvent(
                UUID.randomUUID().toString(),
                id,
                name,
                email,
                Instant.now()
        ));
    }

    private void raiseEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> getEvents() {
        return List.copyOf(events);
    }

}
