package com.huntercodexs.oauth2serverdemo.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomAuthenticationManager implements AuthenticationManager {

    @Value("${huntercodexs.basic-auth.username}")
    String usernameAuthenticate;

    @Value("${huntercodexs.basic-auth.password}")
    String passwordAuthenticate;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String usernameAuth = usernameAuthenticate;
        String passwordAuth = passwordAuthenticate;
        String usernameCred = authentication.getPrincipal().toString();
        String passwordCred = authentication.getCredentials().toString();

        log.debug("CustomAuthenticationManager Authentication authenticate STARTED");
        log.debug("Authentication: " + authentication);

        if (usernameCred.equals(usernameAuth) && isValidCredentials(passwordCred, passwordAuth)) {
            customUserDetailsService.setUserCredentials(0, usernameCred, passwordCred);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(usernameCred);
            return new UsernamePasswordAuthenticationToken(userDetails, passwordCred, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Invalid Credentials");

    }

    protected boolean isValidCredentials(String passwordCredentials, String passwordAuthenticate) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String passwordBCrypt = bCryptPasswordEncoder.encode(passwordAuthenticate);

        log.debug("CustomAuthenticationManager isValidCredentials STARTED");
        log.debug("isValidCredentials passwordCredentials: " + passwordCredentials);
        log.debug("isValidCredentials passwordAuthenticate: " + passwordAuthenticate);
        log.debug("isValidCredentials bCryptPasswordEncoder: " + bCryptPasswordEncoder);
        log.debug("passwordBCrypt: " + passwordBCrypt);

        return bCryptPasswordEncoder.matches(passwordCredentials, passwordBCrypt);

    }

}
