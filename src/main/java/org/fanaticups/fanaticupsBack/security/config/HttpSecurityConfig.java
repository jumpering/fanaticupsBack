package org.fanaticups.fanaticupsBack.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http) throws Exception{
        http
            .csrf( csrfConfig -> csrfConfig.disable() )
            //.csrf(AbstractHttpConfigurer::disable) //to avoid 403 forbiden error on post
            .sessionManagement( sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
            .authenticationProvider(authenticationProvider)
            .authorizeHttpRequests( authConfig -> {
                authConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                authConfig.requestMatchers("/error").permitAll();//endpoint por defecto springboot
                authConfig.anyRequest().permitAll();
            } );
        return http.build();

    }
    
}
