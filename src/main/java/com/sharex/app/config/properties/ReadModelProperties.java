package com.sharex.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "read-model")
public class ReadModelProperties {
    private String updateStrategy;
}
