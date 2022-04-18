package com.huntercodexs.oauth2serverdemo.config;

import lombok.extern.slf4j.Slf4j;
import com.huntercodexs.oauth2serverdemo.config.security.CustomAuthenticationManager;
import com.huntercodexs.oauth2serverdemo.config.security.CustomClientDetailsService;
import com.huntercodexs.oauth2serverdemo.config.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth.server.custom.endpoint}")
    private String oauth2CustomEndpoint;
	
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;

    @Autowired
    private CustomClientDetailsService customClientDetailsService;
    
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
        log.debug("PasswordEncoder: AuthorizationServerConfig");
		return NoOpPasswordEncoder.getInstance();
	}
	
    @Bean
    public TokenStore tokenStore() {
        log.debug("tokenStore: AuthorizationServerConfig");
        return new InMemoryTokenStore();
    }
    
    @Bean
    @Autowired
    public UserApprovalHandler userApprovalHandler(TokenStore tokenStore) {

        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(customClientDetailsService));
        handler.setClientDetailsService(customClientDetailsService);

        log.debug("userApprovalHandler: AuthorizationServerConfig");
        log.debug("handler: " + handler);

        return handler;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .pathMapping("/oauth/token", oauth2CustomEndpoint+"/token" )
                .pathMapping("/oauth/check_token", oauth2CustomEndpoint+"/check_token")
                .tokenStore(this.tokenStore)
                .userApprovalHandler(this.userApprovalHandler)
                .authenticationManager(customAuthenticationManager)
                .userDetailsService(customUserDetailsService);

        log.debug("CONFIGURE 1: AuthorizationServerConfig");
        log.debug("endpoints: " + endpoints.toString());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);

        log.debug("CONFIGURE 2: AuthorizationServerConfig");
        log.debug("clients: " + clients.toString());
        log.debug("clients.inMemory: " + clients.inMemory().toString());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {

        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(this.passwordEncoder);

        log.debug("CONFIGURE 3: AuthorizationServerConfig");
        log.debug("security: " + security.toString());
    }
    
    @Configuration
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

       @Override
       public void configure(final HttpSecurity http) throws Exception {

    	   http.authorizeRequests()
                   .antMatchers("/huntercodexs/**").permitAll().anyRequest().authenticated();

           log.debug("CONFIGURE 4: ResourceServerConfiguration");
           log.debug(http.toString());

       }
       
    }

}