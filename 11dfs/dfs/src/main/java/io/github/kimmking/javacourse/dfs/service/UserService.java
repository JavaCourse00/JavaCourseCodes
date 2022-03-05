package io.github.kimmking.javacourse.dfs.service;

import io.github.kimmking.javacourse.dfs.mapper.UserMapper;
import io.github.kimmking.javacourse.dfs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User create(User user){
        int result = this.userMapper.create(user);
        System.out.println("create user[" + user + "],result=" + result);
        return result>0 ? user : null;
    }

    public User findById(Long id){
        return this.userMapper.findById(id);
    }
}
