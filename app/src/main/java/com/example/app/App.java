package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }



    // ==== 测试自动配置 ====
    @Autowired
    WebInfo info;

    @Bean
    public void printInfo(){
        System.out.println(info.getName());
    }

}
