package com.sharex.app.infrastructure.persistence.config;

import com.sharex.app.infrastructure.messaging.outbox.OutboxEventPublisherAdapter;
import com.sharex.app.shared.event.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OutboxConfig {

    private final OutboxEventPublisherAdapter adapter;

    public OutboxConfig(OutboxEventPublisherAdapter adapter) {
        this.adapter = adapter;
    }

    @Bean
    @Primary
    public EventPublisher eventPublisher() {
        return adapter;
    }
}
