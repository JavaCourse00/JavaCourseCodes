package io.kimmking.rpcfx.server;

import io.kimmking.rpcfx.api.User;
import io.kimmking.rpcfx.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
