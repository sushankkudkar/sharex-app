package com.sharex.app.shared.event;

public interface EventPublisher {
    void publish(DomainEvent event);
}
