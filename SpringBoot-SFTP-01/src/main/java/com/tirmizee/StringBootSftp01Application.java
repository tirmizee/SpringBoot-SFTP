package com.tirmizee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.config.EnableIntegration;

import com.tirmizee.service.SftpService;

@EnableIntegration
@SpringBootApplication
public class StringBootSftp01Application implements CommandLineRunner {

	@Autowired
	private ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication.run(StringBootSftp01Application.class, args);
	}

	@Override
	public void run(String...args) throws Exception {
		SftpService sftpService = applicationContext.getBean(SftpService.class);
//		sftpService.upload();
		sftpService.download();
	}

}
