package com.tirmizee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.tirmizee.service.SftpService;

@SpringBootApplication
public class StringBootSftp02CachingSessionApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication.run(StringBootSftp02CachingSessionApplication.class, args);
	}

	@Override
	public void run(String...args) throws Exception {
		SftpService sftpService = applicationContext.getBean(SftpService.class);
		for (int i = 0; i < 100; i++) {
			sftpService.upload();
		}
		
	}
	
}
