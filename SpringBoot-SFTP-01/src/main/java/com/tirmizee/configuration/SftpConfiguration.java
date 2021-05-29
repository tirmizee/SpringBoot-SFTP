package com.tirmizee.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import com.jcraft.jsch.ChannelSftp.LsEntry;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConditionalOnProperty(
    value="sftp.client.enabled", 
    havingValue = "true", 
    matchIfMissing = false)
@ConfigurationProperties(prefix = "sftp.client")
public class SftpConfiguration {
	
	private String host;
	private int port;
	private String username;
	private String password;
	private boolean allowUnknowKey;
	private boolean sharedSession;
	
	@Bean
	public SessionFactory<LsEntry> sftpSessionFactory() {
		DefaultSftpSessionFactory sftpSessionFactory = new DefaultSftpSessionFactory(sharedSession);
		sftpSessionFactory.setHost(host);
		sftpSessionFactory.setPort(port);
		sftpSessionFactory.setUser(username);
		sftpSessionFactory.setPassword(password);
		sftpSessionFactory.setTimeout(10000);
		sftpSessionFactory.setAllowUnknownKeys(allowUnknowKey);
		return sftpSessionFactory;
	}
	
}
