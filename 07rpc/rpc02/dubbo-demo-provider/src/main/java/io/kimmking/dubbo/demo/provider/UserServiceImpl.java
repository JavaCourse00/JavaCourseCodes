package io.kimmking.dubbo.demo.provider;

import io.kimmking.dubbo.demo.api.User;
import io.kimmking.dubbo.demo.api.UserService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
