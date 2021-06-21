package com.example.demo.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/list")
    public String list() {
        return "KK-" + System.currentTimeMillis();
    }

}
