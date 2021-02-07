package io.order.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

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
public class ActiveMqQueueConsumer {
    public static final String ACTIVE_MQ_ORDER = "order.activeMQ";

    @JmsListener(destination = ACTIVE_MQ_ORDER)
    public void receiveMessage(Order order) {
        order.setStatusId(1);
        log.info("<=========== 收到消息" + order.toString());
    }
}
