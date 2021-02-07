package io.byk.queue.order.service;

import static io.byk.config.ActiveMqConfig.QUEUE_SIZE;

import java.util.Objects;
import java.util.Queue;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.byk.queue.order.entity.Order;
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
    public String sendMessage(Queue<Order> orderQueue) throws IllegalAccessException {
        // STEP 1: 校验队列
        if (Objects.isNull(orderQueue)) {
            throw new IllegalAccessException("订单队列不存在");
        }
        boolean isOrderQueueEmpty = QUEUE_SIZE.equals(orderQueue.size());
        if (isOrderQueueEmpty) {
            return "订单队列已满";
        }
        // STEP 2: 生成订单
        String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        Order order = new Order(orderId, 0);
        // STEP 3: 发送订单消息
        orderQueue.add(order);
        return order.toString();
    }
}
