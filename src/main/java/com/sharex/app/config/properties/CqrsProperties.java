package com.sharex.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cqrs")
public class CqrsProperties {

    private WriteDatasource writeDatasource = new WriteDatasource();
    private ReadDatasource readDatasource = new ReadDatasource();
    private EventHandler eventHandler = new EventHandler();

    @Data
    public static class WriteDatasource {
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class ReadDatasource {
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class EventHandler {
        private boolean async;
    }
}
