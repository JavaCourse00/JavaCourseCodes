package com.example.app;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
public class WebProperties {

    private String a = "aaa";

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
