package com.tirmizee.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@Service
public class sftpServiceImpl implements SftpService {
	
	public final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SessionFactory<LsEntry> sftpSessionFactory;
	
	@Override
	public void upload() {
		
		Session<LsEntry> sftpSession = sftpSessionFactory.getSession();
		
		try {
			
			File file = File.createTempFile("hello", ".txt");
			sftpSession.write(new FileInputStream(file), "/upload/" + file.getName());
			
		} catch (Exception e) {
			log.error("upload error -> {}", e);
		} finally {
//			sftpSession.close();
		}
	}

	@Override
	public void download() {
		try {
			Session<LsEntry> sftpSession = sftpSessionFactory.getSession();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			sftpSession.read("/upload/hello6722876988669945961.txt", outputStream);
			log.info("read -> {}" , new String(outputStream.toByteArray()));
		} catch (Exception e) {
			log.error("upload error -> {}", e);
		} 
	}

}
