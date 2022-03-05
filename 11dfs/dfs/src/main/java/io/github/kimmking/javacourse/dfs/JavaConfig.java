package io.github.kimmking.javacourse.dfs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class JavaConfig {
    @Bean
    CommonsMultipartResolver createCommonsMultipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(4*1024*1024);
        commonsMultipartResolver.setMaxInMemorySize(4*1024*1024);
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        return commonsMultipartResolver;
    }
}
