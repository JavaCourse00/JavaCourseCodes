package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.annotation.RpcfxReference;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/1/14 00:35
 */

@RestController
public class HelloController {
    @RpcfxReference
    UserService userService2;// = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());

    @GetMapping("/api/hello")
    public User invoke() {
        return userService2.findById(100);
    }

}
