package com.huntercodexs.oauth2serverdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class OAuth2ServerDemoApplication {

	public static void main(String[] args) {
		log.info("\n\n\n");
		log.info("---------------------------------------------------------------------------------------------");
		log.debug("OAUTH2-SERVER-DEMO STARTED");
		SpringApplication.run(OAuth2ServerDemoApplication.class, args);
		log.debug("OAUTH2-SERVER-DEMO IS RUNNING");
	}
}
