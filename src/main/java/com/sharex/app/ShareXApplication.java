package com.sharex.app;

import com.sharex.app.config.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
        CqrsProperties.class,
        CustomProperties.class
})
@EnableScheduling
public class ShareXApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareXApplication.class, args);
	}

}
