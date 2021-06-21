package com.example.demo.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/hello")
    public String hello() {
        return "KK-" + System.currentTimeMillis();
    }

}
