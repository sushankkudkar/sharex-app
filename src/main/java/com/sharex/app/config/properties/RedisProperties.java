package com.sharex.app.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "custom.redis")
public class RedisProperties {

    private String mode = "single";         // single | sentinel | cluster
    private String host = "localhost";
    private int port = 6379;

    // sentinel mode
    private String master;
    private String[] nodes;

    // cluster mode
    private String[] clusterNodes;

    // pool/connection settings
    private int poolSize = 64;
    private int minIdleSize = 16;
    private int timeout = 10000;
    private int retryInterval = 1500;
    private int retryAttempts = 3;
}
