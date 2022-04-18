package com.huntercodexs.oauth2serverdemo.config.security;

import com.huntercodexs.oauth2serverdemo.model.OperatorEntity;
import com.huntercodexs.oauth2serverdemo.model.RoleEntity;
import com.huntercodexs.oauth2serverdemo.repository.OauthClientRepository;
import com.huntercodexs.oauth2serverdemo.repository.OperatorRepository;
import com.huntercodexs.oauth2serverdemo.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    /**TO USE FROM FILE PROPERTIES*/
    /*@Value("${huntercodexs.basic-auth.username}")
    String usernameAuthenticate;
    @Value("${huntercodexs.basic-auth.password}")
    String passwordAuthenticate;
    @Value("${huntercodexs.basic-auth.role}")
    int roleAuthenticate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String usernameAuth = usernameAuthenticate;
        String passwordAuth = passwordAuthenticate;
        String usernameCred = authentication.getPrincipal().toString();
        String passwordCred = authentication.getCredentials().toString();
        int roleAuth = roleAuthenticate;

        log.debug(">>> CustomAuthenticationManager Authentication authenticate STARTED");
        log.debug(">>> Authentication: " + authentication);

        if (usernameCred.equals(usernameAuth) && isValidCredentials(passwordCred, passwordAuth)) {
            customUserDetailsService.setUserCredentials(roleAuth, usernameCred, passwordCred);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(usernameCred);
            return new UsernamePasswordAuthenticationToken(userDetails, passwordCred, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Invalid Credentials");

    }*/

    /**TO USE FROM DATABASE*/
    @Autowired
    OauthClientRepository oauthClientRepository;
    @Autowired
    OperatorRepository operatorRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String usernameCred = authentication.getPrincipal().toString();
        log.debug(">>> usernameCred: " + usernameCred);

        String passwordCred = authentication.getCredentials().toString();
        log.debug(">>> passwordCred: " + passwordCred);

        if (usernameCred.equals("")) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        try {

            OperatorEntity operatorEntity = operatorRepository.findByUsername(usernameCred);
            log.debug(">>> operatorEntity: " + operatorEntity);

            if (operatorEntity == null || operatorEntity.getUsername().equals("")) {
                throw new BadCredentialsException("Invalid Credentials");
            }

            log.debug(">>> CustomAuthenticationManager Authentication authenticate STARTED");
            log.debug(">>> Authentication: " + authentication);

            if (isValidCredentials(passwordCred, operatorEntity.getPassword())) {

                RoleEntity roleEntity = roleRepository.findRole(operatorEntity.getRole());
                log.debug(">>> roleEntity: " + roleEntity);

                customUserDetailsService.setUserCredentialsFromDatabase(roleEntity.getName(), usernameCred, passwordCred);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(usernameCred);
                return new UsernamePasswordAuthenticationToken(userDetails, passwordCred, userDetails.getAuthorities());
            }

        } catch (BadCredentialsException bc) {
            bc.printStackTrace();
        }

        throw new BadCredentialsException("Invalid Credentials");

    }

    protected boolean isValidCredentials(String passwordCredentials, String passwordAuthenticate) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String passwordBCrypt = bCryptPasswordEncoder.encode(passwordAuthenticate);

        log.debug(">>> CustomAuthenticationManager isValidCredentials STARTED");
        log.debug(">>> isValidCredentials passwordCredentials: " + passwordCredentials);
        log.debug(">>> isValidCredentials passwordAuthenticate: " + passwordAuthenticate);
        log.debug(">>> isValidCredentials bCryptPasswordEncoder: " + bCryptPasswordEncoder);
        log.debug(">>> passwordBCrypt: " + passwordBCrypt);

        return bCryptPasswordEncoder.matches(passwordCredentials, passwordBCrypt);

    }

}
