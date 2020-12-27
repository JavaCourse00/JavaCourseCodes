package io.kimmking.cache.controller;

import io.kimmking.cache.entity.User;
import io.kimmking.cache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableAutoConfiguration
public class UserController {

    @Autowired
    UserService userService;
    
    @RequestMapping("/user/find")
    User find(Integer id) {
        return userService.find(id);
        //return new User(1,"KK", 28);
    }

    @RequestMapping("/user/list")
    List<User> list() {
        return userService.list();
//        return Arrays.asList(new User(1,"KK", 28),
//                             new User(2,"CC", 18));
    }
}