package com.huntercodexs.oauth2serverdemo.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.huntercodexs.oauth2serverdemo.config.security.RoleOperator.*;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private String usernameCred;
	private String passwordCred;
	private List<GrantedAuthority> authorities;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		LoggedOperator loggedOperator = new LoggedOperator(this.usernameCred, this.passwordCred, this.authorities);

		UserDetails user = new User(
				username,
				loggedOperator.getPassword(),
				true,
				true,
				true,
				true,
				AuthorityUtils.NO_AUTHORITIES);

		log.debug("CustomUserDetailsService->loadUserByUsername STARTED");
		log.debug("username: " + username);
		log.debug("LoggedOperator: " + loggedOperator);
		log.debug("CustomUserDetailsService->loadUserByUsername[UserDetails]: " + user);

		if (user.getUsername().equals("") || !user.isEnabled()) {

			log.warn("CustomUserDetailsService->loadUserByUsername: user.getUsername().equals('') || !user.isEnabled()");

			return new User(
					user.getUsername(),
					user.getPassword(),
					!user.isEnabled(),
					user.isAccountNonExpired(),
					user.isCredentialsNonExpired(),
					user.isAccountNonLocked(),
					AuthorityUtils.NO_AUTHORITIES);
		}

		log.debug("CustomUserDetailsService->loadUserByUsername FINISHED");

		return loggedOperator;

	}

	public void setUserCredentials(int userLevel, String usernameCred, String passwordCred) {

		String roles;

		switch (userLevel) {
			case 0:
				roles = ROLE_ADMIN.name();
				break;
			case 1:
				roles = ROLE_USER.name();
				break;
			case 2:
				roles = ROLE_CLIENT.name();
				break;
			case 3:
				roles = ROLE_OPERATOR.name();
				break;
			case 4:
				roles = ROLE_MODERATOR.name();
				break;
			default:
				roles = "";

		}

		this.usernameCred = usernameCred;
		this.passwordCred = passwordCred;
		this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

		log.debug("CustomUserDetailsService->setUserRoles");
		log.debug("ROLES: " + roles);
		log.debug("this.authorities: " + this.authorities);
	}

}

