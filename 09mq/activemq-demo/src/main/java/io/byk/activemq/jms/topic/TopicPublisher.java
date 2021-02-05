package io.byk.activemq.jms.topic;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 主题生产者
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Service
@Slf4j
public class TopicPublisher {
    // JMS 模板
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessage(String destinationName, String message) {
        log.info("============> 发送 Topic 消息" + message);
        Destination destination = new ActiveMQTopic(destinationName);
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

}
