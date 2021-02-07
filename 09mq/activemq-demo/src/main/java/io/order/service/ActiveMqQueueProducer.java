package io.order.service;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import io.order.model.Order;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 队列生产者
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Service
@Slf4j
@Getter
public class ActiveMqQueueProducer {
    // JMS 模板
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessage(String destinationName, String orderId) {
        Destination destination = new ActiveMQQueue(destinationName);
        // STEP 1: 生成订单
        Order order = new Order(0, orderId, 0);
        jmsMessagingTemplate.convertAndSend(destination, order);
        log.info("============> 发送 Queue 消息" + order.toString());
    }

}
