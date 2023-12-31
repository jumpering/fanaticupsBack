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
        //WebMvcConfigurer.super.addResourceHandlers(registry);
        registry
                .addResourceHandler("/images/**")
                //.addResourceLocations("/resources/");
                .addResourceLocations("file:/Users/xaviergomezcanals/Documents/Projects/fanaticups/images/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
