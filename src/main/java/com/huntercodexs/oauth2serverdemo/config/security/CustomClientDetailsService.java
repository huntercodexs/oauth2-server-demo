package com.huntercodexs.oauth2serverdemo.config.security;

import com.huntercodexs.oauth2serverdemo.model.OauthClientEntity;
import com.huntercodexs.oauth2serverdemo.repository.OauthClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static com.huntercodexs.oauth2serverdemo.config.security.RoleOperator.ROLE_CLIENT;

@Service
@Slf4j
public class CustomClientDetailsService implements ClientDetailsService {

	@Autowired
    OauthClientRepository oauthClientRepository;

    @Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        BaseClientDetails details = new BaseClientDetails();
        OauthClientEntity oauthClientEntity = oauthClientRepository.findByClient(clientId);
        String secret = new String(Base64.getDecoder().decode(oauthClientEntity.getPassword()));

        details.setClientId(clientId);
        details.setClientSecret(secret);
        details.setAuthorizedGrantTypes(Arrays.asList("password", "authorization_code", "refresh_token"));
        details.setScope(Arrays.asList("read", "write")); //trust
        details.setResourceIds(Arrays.asList("oauth2-resource"));
        details.setAccessTokenValiditySeconds(oauthClientEntity.getAccessTokenValiditySeconds());
        details.setRefreshTokenValiditySeconds(oauthClientEntity.getRefreshTokenValiditySeconds());

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_CLIENT.name()));

        details.setAuthorities(authorities);

        log.debug(">>> CustomClientDetailsService->loadClientByClientId STARTED");
        log.debug(">>> CLIENT-ID: " + clientId);
        log.debug(">>> OauthClientEntity: " + oauthClientEntity);
        log.debug(">>> secret: " + secret);
        log.debug(">>> authorities: " + authorities);
        log.debug(">>> details: " + details);
        log.debug(">>> CustomClientDetailsService->loadClientByClientId FINISHED");

        return details;
	}
	
}
