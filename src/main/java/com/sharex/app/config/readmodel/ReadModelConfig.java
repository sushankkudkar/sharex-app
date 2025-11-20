package com.sharex.app.config.readmodel;

import com.sharex.app.config.properties.ReadModelProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ReadModelConfig {

    private final ReadModelProperties props;

    @Bean
    public String readModelStrategy() {
        return props.getUpdateStrategy();
    }
}
