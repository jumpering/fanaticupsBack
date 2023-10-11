package org.fanaticups.fanaticupsBack.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.fanaticups.fanaticupsBack.security.service.UserDetailsServiceImpl;


@Configuration
public class WebSecurityConfig {
    
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception{
   http
        .authorizeHttpRequests((authorize) -> authorize
        .requestMatchers("/**").permitAll()
        .requestMatchers("/test/**").permitAll()
        .requestMatchers("/create/**").permitAll()
            .requestMatchers("/cups/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        );

    http
        .formLogin(withDefaults()); // (1)
    http
        .httpBasic(withDefaults()); // (1)

    return http.build();
    }

    @Bean
    UserDetailsServiceImpl userDetailsServiceImpl(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // "secreto" => [BCrypt] => "$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq"
}
