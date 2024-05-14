package org.fanaticups.fanaticupsBack.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

@Bean
  public WebMvcConfigurer corsConfigurer(){
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**")
        .allowedOrigins("http://fanaticups.org, https://fanaticups.org, http://www.fanaticups.org, https://www.fanaticups.org, http://5.250.184.31:4200, http://5.250.184.31, http://localhost:4200")
        .allowedMethods("*")
        .exposedHeaders("*");
      }
    };
  }
}
