package com.huntercodexs.oauth2serverdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@Slf4j
public class CorsConfiguration implements  WebMvcConfigurer  {
	
	@Value("#{'${cors.allowed.origins}'.split(',')}")
	private List<String> rawOrigins;	
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		log.debug(">>> ADD CORS MAPPING STARTED");

		registry.addMapping("/**")
			//.allowedOrigins(getOrigin())
			.allowedOrigins("*")
			//.allowedMethods("POST", "GET",  "PUT", "DELETE", "OPTIONS")
			.allowedMethods("*")
			//.allowedHeaders("X-Auth-Token", "Content-Type")
			.allowedHeaders("*")
			//.exposedHeaders("custom-header1", "custom-header2")
			.allowCredentials(true)
			.maxAge(4800);

		log.debug(registry.toString());
		log.debug(">>> ADD CORS MAPPING FINISHED");

	}
	
	public String[] getOrigin() {

	    int size = rawOrigins.size();
	    String[] originArray = new String[size];

		return rawOrigins.toArray(originArray);

	}	
	
}
