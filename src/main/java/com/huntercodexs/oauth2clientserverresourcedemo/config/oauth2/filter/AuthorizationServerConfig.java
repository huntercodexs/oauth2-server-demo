package com.huntercodexs.oauth2clientserverresourcedemo.config.oauth2.filter;

import com.huntercodexs.oauth2clientserverresourcedemo.config.oauth2.security.CustomAuthenticationManager;
import com.huntercodexs.oauth2clientserverresourcedemo.config.oauth2.security.CustomClientDetailsService;
import com.huntercodexs.oauth2clientserverresourcedemo.config.oauth2.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableAuthorizationServer
@SuppressWarnings("deprecation")
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
		return NoOpPasswordEncoder.getInstance();
	}
	
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
    
    @Bean
    @Autowired
    public UserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(customClientDetailsService));
        handler.setClientDetailsService(customClientDetailsService);

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
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(this.passwordEncoder);
    }
    
    @Configuration
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Value("${api.prefix}")
        private String apiPrefix;

       @Override
       public void configure(final HttpSecurity http) throws Exception {
           http.authorizeRequests()
                   /*Main Services*/
                   .antMatchers(apiPrefix+"/products").authenticated()
                   /*Others Services*/
                   .antMatchers(apiPrefix+"/samples").authenticated()
                   /*Open Services*/
                   .antMatchers(apiPrefix+"/welcome").permitAll()
                   /*Swagger*/
                   .antMatchers("/swagger-ui/**").permitAll()
                   .antMatchers("/api-docs/**").permitAll()
                   .antMatchers("/api-docs.yaml").permitAll()
                   /*Actuator*/
                   .antMatchers("/actuator/**").permitAll().anyRequest().authenticated();

       }
       
    }

}