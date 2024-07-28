package org.fanaticups.fanaticupsBack.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import org.fanaticups.fanaticupsBack.security.filters.JWTAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    @Autowired
    JWTAuthenticationFilter JWTAuthorizationFilter;

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {

        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable) //to avoid 403 forbiden error on post
                //.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/api/authenticate/**").permitAll()
                                //.requestMatchers("/api/register/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/cups/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/cups/**").authenticated()
                                .requestMatchers(HttpMethod.PUT,"/api/cups/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE,"/api/cups/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/chat/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/chat/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories/**").authenticated()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                                //.requestMatchers("/authenticate/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH").permiteAll()
                )
                .addFilterBefore(JWTAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // "secreto" => [BCrypt] => "$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq"
}
