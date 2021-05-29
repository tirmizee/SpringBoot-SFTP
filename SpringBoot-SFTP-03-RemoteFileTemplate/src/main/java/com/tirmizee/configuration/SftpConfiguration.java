package com.tirmizee.configuration;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

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
	public SessionFactory<LsEntry> sessionFactory() {
		DefaultSftpSessionFactory sessionFactory = new DefaultSftpSessionFactory(sharedSession);
		sessionFactory.setHost(host);
		sessionFactory.setPort(port);
		sessionFactory.setUser(username);
		sessionFactory.setPassword(password);
		sessionFactory.setTimeout(10000);
		sessionFactory.setAllowUnknownKeys(allowUnknowKey);
		return sessionFactory;
	}
	
	@Bean 
	public CachingSessionFactory<LsEntry> cachingSessionFactory(SessionFactory<LsEntry> sessionFactory) {
		CachingSessionFactory<LsEntry> cachingSessionFactory = new CachingSessionFactory<LsEntry>(sessionFactory);
		cachingSessionFactory.setPoolSize(10);
		cachingSessionFactory.setTestSession(true);
		cachingSessionFactory.setSessionWaitTimeout(60000);
		return cachingSessionFactory;
	}
	
	@Bean 
	public SftpRemoteFileTemplate sftpRemoteFileTemplate(CachingSessionFactory<LsEntry> cachingSessionFactory) {
		SftpRemoteFileTemplate remoteFileTemplate = new SftpRemoteFileTemplate(cachingSessionFactory);
		remoteFileTemplate.setAutoCreateDirectory(true);
		remoteFileTemplate.setCharset(StandardCharsets.UTF_8.name());
		remoteFileTemplate.setRemoteDirectoryExpression(new LiteralExpression("/upload/"));
		return remoteFileTemplate;
	}
	
//	@Bean
//	public SftpInboundFileSynchronizer sftpInboundFileSynchronizer(SessionFactory<LsEntry> sftpSessionFactory) {
//		SftpInboundFileSynchronizer inboundFileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory);
//		inboundFileSynchronizer.setDeleteRemoteFiles(false);
//		inboundFileSynchronizer.setRemoteDirectory("/upload");
//		return inboundFileSynchronizer;
//	}

}
