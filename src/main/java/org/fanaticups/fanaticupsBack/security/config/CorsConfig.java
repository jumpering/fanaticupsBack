package org.fanaticups.fanaticupsBack.security.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  // @Bean
  // CorsConfigurationSource corsConfigurationSource() {
  //   CorsConfiguration configuration = new CorsConfiguration();
  //   configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
  //   configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
  //   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  //   source.registerCorsConfiguration("/**", configuration);
  //   return source;
  // }


@Bean
  public WebMvcConfigurer corsConfigurer(){
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**")
        //.allowedOrigins("http://localhost:4200")

        .allowedOrigins("https://fanaticups.onrender.com, http://fanaticups.org, http://www.fanaticups.org, http://5.250.184.31:4200, http://5.250.184.31, https://fanaticups.onrender.com/files, http://localhost:4200")
        .allowedMethods("*")
        .exposedHeaders("*");



        // registry.addMapping("/authenticate")
        // .allowedOrigins("http://localhost:4200")
        // .allowedMethods("*")
        // .exposedHeaders("*");


        // registry.addMapping("/test")
        // .allowedOrigins("http://localhost:4200")
        // .allowedMethods("*")
        // .exposedHeaders("*");


        // registry.addMapping("/cups")
        // .allowedOrigins("http://localhost:4200")
        // .allowedMethods("*");

        // registry.addMapping("/")
        // .allowedOrigins("http://localhost:4200")
        // .allowedMethods("*");
      }
    };
  }
}
