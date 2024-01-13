package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.annotation.RpcfxService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("io.kimmking.rpcfx.demo.api.UserService")
@RpcfxService
public class UserServiceImpl implements UserService {

    @Autowired
    Environment environment;

    @Override
    public User findById(int id) {
        return new User(id, "KK-"
                + environment.getProperty("server.port") + "_" + System.currentTimeMillis());
    }
}
