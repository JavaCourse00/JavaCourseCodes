package io.byk.activemq.topic;

import static io.byk.activemq.config.ActiveMqConfig.ACTIVE_MQ_TOPIC;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 主题消费者
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Service
@Slf4j
public class TopicSubscriber {
    @JmsListener(destination = ACTIVE_MQ_TOPIC, containerFactory = "myJmsContainerFactory")
    public void receiveMessage(String message) {
        log.info("<=========== 收到消息" + message);
    }
}
