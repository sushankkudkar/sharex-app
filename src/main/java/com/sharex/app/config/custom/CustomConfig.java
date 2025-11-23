package com.sharex.app.config.custom;

import com.sharex.app.config.properties.CustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomConfig {

    private final CustomProperties props;

    @Bean
    public String appEnvironment() {
        log.info("CustomConfig Loaded: environment={}, apiOwner={}, metricsEnabled={}",
                props.getEnvironment(),
                props.getApiOwner(),
                props.isEnableMetrics()
        );

        return props.getEnvironment();
    }
}
