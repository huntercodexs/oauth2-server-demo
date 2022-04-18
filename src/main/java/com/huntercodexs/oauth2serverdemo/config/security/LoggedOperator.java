package com.huntercodexs.oauth2serverdemo.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Slf4j
public class LoggedOperator extends User {

	public LoggedOperator(String usernameCred, String passwordCred, List<GrantedAuthority> authorities) {
		super(usernameCred, passwordCred, authorities);

		log.debug("CONSTRUCTOR: LoggedOperator");
		log.debug("usernameCred: " + usernameCred);
		log.debug("passwordCred: " + passwordCred);
		log.debug("authorities: " + authorities);
	}

}
