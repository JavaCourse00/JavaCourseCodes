package io.byk.queue.order.service;

import java.util.Objects;
import java.util.Queue;

import org.springframework.stereotype.Service;

import io.byk.queue.order.entity.Order;
import lombok.extern.slf4j.Slf4j;

/**
 * 队列消费者
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Service
@Slf4j
public class QueueConsumer {
    public String receiveMessage(Queue<Order> orderQueue) throws IllegalAccessException {
        // STEP 1: 校验队列
        if (Objects.isNull(orderQueue)) {
            throw new IllegalAccessException("订单队列不存在");
        }
        if (orderQueue.isEmpty()) {
            throw new IllegalStateException("订单队列为空");
        }
        // STEP 2: 校验订单
        Order order = orderQueue.poll();
        if (Objects.isNull(order)) {
            throw new IllegalAccessException("订单不存在");
        }
        boolean isOrderComplete = order.getStatusId() == 1;
        if (isOrderComplete) {
            throw new IllegalAccessException("订单已完成");
        }
        // STEP 3: 更改订单状态
        order.setStatusId(1);
        return order.toString();
    }
}
