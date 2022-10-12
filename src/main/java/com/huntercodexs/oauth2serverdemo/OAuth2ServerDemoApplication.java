package com.huntercodexs.oauth2serverdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
public class OAuth2ServerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2ServerDemoApplication.class, args);
	}

}
