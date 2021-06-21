package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebConfiguration.class)
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    @Autowired
    WebProperties properties;

    @Autowired
    WebConfiguration configuration;

    @Bean
    public WebInfo creatInfo(){
        return new WebInfo(configuration.name + "-"+properties.getA());
    }

}
