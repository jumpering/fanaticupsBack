package org.fanaticups.fanaticupsBack.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
//                .addResourceHandler("/images/**")
//                .addResourceLocations("file:/Users/xaviergomezcanals/Documents/Projects/fanaticups/images/");
                .addResourceHandler("/images/**")
                .addResourceLocations("https://5.250.190.45/images/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
