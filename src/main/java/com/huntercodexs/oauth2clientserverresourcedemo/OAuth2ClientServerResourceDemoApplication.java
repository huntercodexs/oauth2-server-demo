package com.huntercodexs.oauth2clientserverresourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
public class OAuth2ClientServerResourceDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2ClientServerResourceDemoApplication.class, args);
	}
}
