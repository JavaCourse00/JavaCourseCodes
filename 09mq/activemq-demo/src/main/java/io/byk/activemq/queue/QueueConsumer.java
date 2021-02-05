package io.byk.activemq.queue;

import static io.byk.activemq.config.ActiveMqConfig.ACTIVE_MQ_QUEUE;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

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
    @JmsListener(destination = ACTIVE_MQ_QUEUE)
    public void receiveMessage(String message) {
        log.info("<=========== 收到消息" + message);
    }
}
