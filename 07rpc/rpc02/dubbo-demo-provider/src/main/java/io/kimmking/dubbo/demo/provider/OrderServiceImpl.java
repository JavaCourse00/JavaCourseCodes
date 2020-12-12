package io.kimmking.dubbo.demo.provider;

import io.kimmking.dubbo.demo.api.Order;
import io.kimmking.dubbo.demo.api.OrderService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService(version = "1.0.0")
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
