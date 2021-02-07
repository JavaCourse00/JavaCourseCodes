package io.order.service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import io.order.mapper.OrderMapper;
import io.order.model.Order;
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
    @Resource
    OrderMapper orderMapper;

    public void receiveMessage() throws IllegalAccessException {
        // STEP 1: 获取为处理订单列表
        List<Order> orderList = orderMapper.selectByStatusId(0);
        if (Objects.isNull(orderList) || orderList.isEmpty()) {
            throw new IllegalStateException("所有订单已处理");
        }
        for (Order order : orderList) {
            // STEP 2: 校验订单
            if (Objects.isNull(order)) {
                throw new IllegalAccessException("订单不存在");
            }
            boolean isOrderComplete = order.getStatusId() == 1;
            if (isOrderComplete) {
                throw new IllegalAccessException("订单已完成");
            }
            // STEP 3: 插入订单
            order.setStatusId(1);
            int success = orderMapper.updateByPrimaryKeySelective(order);
            if (success <= 0L) {
                throw new IllegalAccessException("更新订单失败");
            }
            log.info("订单" + order.getOrderId() + "=======>出队");
        }
    }
}
