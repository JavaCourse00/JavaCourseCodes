package io.kimmking.dtx01.service;

import io.kimmking.dtx01.entity.User;

import java.util.List;

public interface UserService {

    User find(Long id);

    List<User> list();

}
