package io.order.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import io.order.mapper.OrderMapper;
import io.order.model.Order;
import lombok.extern.slf4j.Slf4j;

/**
 * 队列生产者
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Service
@Slf4j
public class QueueProducer {
    @Resource
    OrderMapper orderMapper;

    public String sendMessage(String orderId) throws IllegalAccessException {
        // STEP 1: 生成订单
        Order order = new Order(0, orderId, 0);
        // STEP 2: 发送订单消息
        int success = orderMapper.insertSelective(order);
        if (success <= 0L) {
            throw new IllegalStateException("生成订单失败");
        }
        return "订单" + order.getOrderId();
    }
}
