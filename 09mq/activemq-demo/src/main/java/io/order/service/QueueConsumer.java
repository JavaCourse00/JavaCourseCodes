package io.order.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
    //最大重试次数
    private static final Integer TRY_TIMES = 6;
    //重试间隔时间单位秒
    private static final Long INTERVAL_TIME = 100L;

    public void receiveMessage() {
        // STEP 1: 获取为处理订单列表
        List<Order> orderList = orderMapper.selectByStatusId(0);
        if (Objects.isNull(orderList) || orderList.isEmpty()) {
            throw new IllegalStateException("所有订单已处理");
        }
        for (Order order : orderList) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        // 重试策略
                        int success = retryBuss(order);
                        if (success <= 0) {
                            log.info("订单" + order.getOrderId() + "=======>出队失败");
                        }
                    } catch (InterruptedException exception) {
                        log.info("进程被打断");
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }

    private int retryBuss(Order order) throws InterruptedException {
        int retryNum = 1;
        while (retryNum <= TRY_TIMES) {
            try {
                // 加锁解决并发，效率降低
                int success = updateOrder(order);
                if (success > 0L) {
                    return 1;
                }
                retryNum++;
            } catch (Exception e) {
                retryNum++;
                Thread.sleep(INTERVAL_TIME);
                continue;
            }
        }
        return 0;
    }

    private synchronized int updateOrder(Order order) throws IllegalAccessException, InterruptedException {
        // STEP 1: 校验订单
        if (Objects.isNull(order)) {
            throw new IllegalAccessException("订单不存在");
        }
        if (order.getStatusId() == 1) {
            return 1;
        }
        // STEP 2: 插入订单
        order.setStatusId(1);
        int success = orderMapper.updateByPrimaryKeySelective(order);
        if (success <= 0L) {
            throw new IllegalAccessException("更新订单失败");
        }
        Thread.sleep(100);
        log.info("订单" + order.getOrderId() + "=======>出队");
        return success;
    }
}
