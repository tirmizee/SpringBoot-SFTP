package com.tirmizee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

@EnableIntegration
@SpringBootApplication
public class SpringBootSftp03RemoteFileTemplateApplication implements CommandLineRunner {

	public final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootSftp03RemoteFileTemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		SftpRemoteFileTemplate sftpRemoteFileTemplate = applicationContext.getBean(SftpRemoteFileTemplate.class);
		
		// check exists file
		log.info("exists -> {}", sftpRemoteFileTemplate.exists("/upload/hello610600969339711919.txt"));
		
		// remove file
		log.info("remove -> {}", sftpRemoteFileTemplate.remove("/upload/hello48862251956541128.txt"));
		
		// rename file 
		if(sftpRemoteFileTemplate.exists("/upload/hello49464617754855258.txt")) {
			sftpRemoteFileTemplate.rename("/upload/hello49464617754855258.txt", "/upload/new.txt");
			log.info("exists -> {}", sftpRemoteFileTemplate.exists("/upload/new.txt"));
		}
		
		
	}

}
