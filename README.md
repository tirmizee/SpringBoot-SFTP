# SpringBoot-SFTP

### Setup SFTP

    docker run -v //d/sftp:/home/foo/upload -p 2222:22 -d atmoz/sftp foo:pass:::upload

### Dependencies

    <dependency>
	    <groupId>org.springframework.integration</groupId>
	    <artifactId>spring-integration-sftp</artifactId>
	</dependency>

### Reference

- https://www.youtube.com/watch?v=j0AG4KrzCgs
- https://docs.spring.io/spring-integration/docs/current/reference/html/sftp.html#sftp-session-factory
