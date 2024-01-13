package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.annotation.RpcfxService;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@RpcfxService
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    Environment environment;

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "KK-"
                + environment.getProperty("server.port") + "_Order" + System.currentTimeMillis(), 9.9f);
    }
}
