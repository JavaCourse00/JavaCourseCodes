package io.byk.activemq.queue;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 队列生产者
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Service
@Slf4j
@Getter
public class QueueProducer {
    // JMS 模板
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessage(String destinationName, String message) {
        log.info("============> 发送 Queue 消息" + message);
        Destination destination = new ActiveMQQueue(destinationName);
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

}
