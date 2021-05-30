package com.tirmizee.configuration;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
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
	
	public final Logger log = LoggerFactory.getLogger(getClass());
	
	private String host;
	private int port;
	private String username;
	private String password;
	private boolean allowUnknowKey;
	private boolean sharedSession;
	
	@Bean(name = "sessionFactory")
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
	
	@Bean(name = "cachingSessionFactory")
	public CachingSessionFactory<LsEntry> cachingSessionFactory(@Qualifier("sessionFactory") SessionFactory<LsEntry> sessionFactory) {
		CachingSessionFactory<LsEntry> cachingSessionFactory = new CachingSessionFactory<LsEntry>(sessionFactory);
		cachingSessionFactory.setPoolSize(10);
		cachingSessionFactory.setTestSession(true);
		cachingSessionFactory.setSessionWaitTimeout(60000);
		return cachingSessionFactory;
	}
	
	@Bean(name = "sftpInboundFileSynchronizer")
	public SftpInboundFileSynchronizer sftpInboundFileSynchronizer(@Qualifier("cachingSessionFactory") CachingSessionFactory<LsEntry> cachingSessionFactory) {
		SftpInboundFileSynchronizer inboundFileSynchronizer = new SftpInboundFileSynchronizer(cachingSessionFactory);
		inboundFileSynchronizer.setDeleteRemoteFiles(false);
		inboundFileSynchronizer.setRemoteDirectory("/upload");
		inboundFileSynchronizer.setFilter(new SftpSimplePatternFileListFilter("*.txt"));
		return inboundFileSynchronizer;
	}
	
	@Bean
    @InboundChannelAdapter(channel = "sftpChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> sftpMessageSource(@Qualifier("sftpInboundFileSynchronizer") SftpInboundFileSynchronizer sftpInboundFileSynchronizer) {
        SftpInboundFileSynchronizingMessageSource source = 
        		new SftpInboundFileSynchronizingMessageSource(sftpInboundFileSynchronizer);
        source.setLocalDirectory(new File("ftp-inbound"));
        source.setLoggingEnabled(true);
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
        return source;
    }
	
	@ServiceActivator(inputChannel = "sftpChannel")
	public void handleIncomingFile(File file) {
		log.info("file -> {}", file.getName());
	}
	
//    @Bean
//    @ServiceActivator(inputChannel = "sftpChannel")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//            	log.info("Headers -> {}", message.getHeaders());
//            	log.info("Payload -> {}", message.getPayload());
//            }
//
//        };
//    }

}
