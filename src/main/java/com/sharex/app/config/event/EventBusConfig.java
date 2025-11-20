package com.sharex.app.config.event;

import com.sharex.app.config.properties.EventProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventBusConfig {

    private final EventProperties eventProps;

    @Bean
    public String eventBusType() {
        // could return an InMemoryEventBus or KafkaEventBus later
        return eventProps.getBus();
    }
}
