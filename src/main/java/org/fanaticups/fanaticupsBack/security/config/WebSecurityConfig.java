package org.fanaticups.fanaticupsBack.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.fanaticups.fanaticupsBack.security.filters.JWTAuthorizationFilter;

@Configuration
public class WebSecurityConfig {

    @Autowired
    JWTAuthorizationFilter JWTAuthorizationFilter;

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception{

        http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable) //to avoid 403 forbiden error on post
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/authenticate/**").permitAll()
            .requestMatchers("/test/**").permitAll()
            .requestMatchers("/create/**").permitAll()
            // .requestMatchers("/cups/**").hasRole("ADMIN")
            .requestMatchers("/cups/**").permitAll()
            //.anyRequest().authenticated()
            .anyRequest().permitAll()
        )
        .addFilterBefore(JWTAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

    // http
    //     .formLogin(withDefaults()); // (1)
    // http
    //     .httpBasic(withDefaults()); // (1)

    return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // "secreto" => [BCrypt] => "$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq"
}
