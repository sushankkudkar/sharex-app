package com.sharex.app.config.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.CompositeHealthContributor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthContributor;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DbHealthConfig {

    private final DataSource writeDataSource;
    private final DataSource readDataSource;

    @Bean
    public HealthContributor dbHealthContributor() {

        Map<String, HealthIndicator> indicators = new LinkedHashMap<>();

        indicators.put("write-database", () -> {
            try (var conn = writeDataSource.getConnection()) {
                return Health.up()
                        .withDetail("url", conn.getMetaData().getURL())
                        .build();
            } catch (Exception e) {
                return Health.down(e).build();
            }
        });

        indicators.put("read-database", () -> {
            try (var conn = readDataSource.getConnection()) {
                return Health.up()
                        .withDetail("url", conn.getMetaData().getURL())
                        .build();
            } catch (Exception e) {
                return Health.down(e).build();
            }
        });

        return CompositeHealthContributor.fromMap(indicators);
    }
}
