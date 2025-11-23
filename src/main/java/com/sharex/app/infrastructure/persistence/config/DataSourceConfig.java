package com.sharex.app.infrastructure.persistence.config;

import com.sharex.app.config.properties.CqrsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final CqrsProperties cqrs;

    @Bean(name = "writeDataSource")
    @Primary
    public DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .url(cqrs.getWriteDatasource().getUrl())
                .username(cqrs.getWriteDatasource().getUsername())
                .password(cqrs.getWriteDatasource().getPassword())
                .build();
    }

    @Bean(name = "readDataSource")
    public DataSource readDataSource() {
        return DataSourceBuilder.create()
                .url(cqrs.getReadDatasource().getUrl())
                .username(cqrs.getReadDatasource().getUsername())
                .password(cqrs.getReadDatasource().getPassword())
                .build();
    }
}
