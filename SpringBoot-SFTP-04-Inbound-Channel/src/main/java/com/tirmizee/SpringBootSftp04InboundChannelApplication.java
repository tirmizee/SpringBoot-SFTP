package com.tirmizee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
public class SpringBootSftp04InboundChannelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSftp04InboundChannelApplication.class, args);
	}

}
