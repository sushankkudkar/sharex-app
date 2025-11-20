package com.sharex.app;

import com.sharex.app.config.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CqrsProperties.class,
        ReadModelProperties.class,
        EventProperties.class,
        CustomProperties.class
})
public class ShareXApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareXApplication.class, args);
	}

}
