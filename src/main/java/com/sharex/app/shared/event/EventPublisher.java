package com.sharex.app.shared.event;

import com.sharex.app.infrastructure.messaging.outbox.domain.DomainEvent;

import java.util.List;

public interface EventPublisher {

    /**
     * Legacy method – avoid using this for high throughput.
     * Use publishBatch() instead.
     */
    default void publish(DomainEvent event) {
        publishBatch(List.of(event));
    }

    /**
     * High-throughput: Inserts all events in a single JDBC batch.
     * This is the primary method for production use.
     */
    void publishBatch(List<? extends DomainEvent> events);
}