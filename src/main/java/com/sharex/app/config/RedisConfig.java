package com.sharex.app.config;

import com.sharex.app.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties props;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {

        Config config = new Config();

        switch (props.getMode().toLowerCase()) {

            case "cluster" -> configureCluster(config);
            case "sentinel" -> configureSentinel(config);
            default -> configureSingle(config); // single node (dev/prod small-scale)
        }

        return Redisson.create(config);
    }

    private void configureSingle(Config config) {
        String address = "redis://" + props.getHost() + ":" + props.getPort();

        config.useSingleServer()
                .setAddress(address)
                .setConnectionPoolSize(props.getPoolSize())
                .setConnectionMinimumIdleSize(props.getMinIdleSize())
                .setRetryAttempts(props.getRetryAttempts())
                .setRetryInterval(props.getRetryInterval())
                .setTimeout(props.getTimeout());
    }

    private void configureSentinel(Config config) {
        config.useSentinelServers()
                .setMasterName(props.getMaster())
                .addSentinelAddress(props.getNodes())
                .setMasterConnectionPoolSize(props.getPoolSize())
                .setSlaveConnectionPoolSize(props.getPoolSize())
                .setTimeout(props.getTimeout());
    }

    private void configureCluster(Config config) {
        config.useClusterServers()
                .addNodeAddress(props.getClusterNodes())
                .setScanInterval(2000)
                .setMasterConnectionPoolSize(props.getPoolSize())
                .setSlaveConnectionPoolSize(props.getPoolSize())
                .setTimeout(props.getTimeout());
    }
}
