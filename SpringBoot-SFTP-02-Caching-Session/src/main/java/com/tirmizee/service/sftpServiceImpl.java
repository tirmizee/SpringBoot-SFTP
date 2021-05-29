package com.tirmizee.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@Service
public class sftpServiceImpl implements SftpService {
	
	public final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private CachingSessionFactory<LsEntry> cachingSessionFactory;
	
	@Override
	public void upload() {
		
		Session<LsEntry> sftpSession = cachingSessionFactory.getSession();
		
		try {
			
			File file = File.createTempFile("hello", ".txt");
			FileWriter writer = new FileWriter(file);
		    writer.write("hello world");
		    writer.close();
			sftpSession.write(new FileInputStream(file), "/upload/" + file.getName());
			
		} catch (Exception e) {
			log.error("upload error -> {}", e);
		} finally {
			sftpSession.close();
		}
	}

	@Override
	public void download() {
		try {
			Session<LsEntry> sftpSession = cachingSessionFactory.getSession();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			sftpSession.read("/upload/hello6722876988669945961.txt", outputStream);
			log.info("read -> {}" , new String(outputStream.toByteArray()));
		} catch (Exception e) {
			log.error("upload error -> {}", e);
		} 
	}

	@Override
	public void listNames() {
		try {
			Session<LsEntry> session = cachingSessionFactory.getSession();
			String[] fileNames = session.listNames("/upload");
			for (String fileName : fileNames) {
				log.info("fileName -> {}", fileName);
			}
		} catch (Exception e) {
			log.error("upload error -> {}", e);
		} 
	}

	@Override
	public void listFiles() {
		try {
			Session<LsEntry> session = cachingSessionFactory.getSession();
			LsEntry[] entries = session.list("/upload");
			for (LsEntry entry : entries) {
				log.info("fileName -> {}", entry.getFilename());
			}
		} catch (Exception e) {
			log.error("upload error -> {}", e);
		} 
	}

}
