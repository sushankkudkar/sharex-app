package com.sharex.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "event")
public class EventProperties {
    private String bus;
}
