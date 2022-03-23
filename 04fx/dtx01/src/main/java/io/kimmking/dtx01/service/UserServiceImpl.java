package io.kimmking.dtx01.service;

import io.kimmking.dtx01.entity.User;
import io.kimmking.dtx01.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper; //DAO  // Repository

    public User find(Long id) {
        System.out.println(" ==> find " + id);
        return userMapper.find(id);
    }

    public List<User> list(){
        return userMapper.list();
    }

}
