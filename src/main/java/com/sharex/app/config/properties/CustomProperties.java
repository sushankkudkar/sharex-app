package com.sharex.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
    private String environment;
    private String apiOwner;
    private boolean enableMetrics;
}
