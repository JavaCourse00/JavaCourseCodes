package io.order;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.order.service.ActiveMqQueueConsumer;
import io.order.service.ActiveMqQueueProducer;
import io.order.service.QueueConsumer;
import io.order.service.QueueProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@SpringBootApplication
@Slf4j
public class QueueApplication implements ApplicationRunner {
    @Resource
    QueueProducer queueProducer;

    @Resource
    QueueConsumer queueConsumer;

    @Resource
    ActiveMqQueueProducer activeMqQueueProducer;

    @Resource
    ActiveMqQueueConsumer activeMqQueueConsumer;
    // 测试队列
    public static final String ACTIVE_MQ_ORDER = "order.activeMQ";

    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        for (int i = 0; i < 10; i++) {
            //            log.info(queueProducer.sendMessage(String.valueOf(i)) + "<=======入队");
            activeMqQueueProducer.sendMessage(ACTIVE_MQ_ORDER, String.valueOf(i));
        }
        try {
            queueConsumer.receiveMessage();
        } catch (IllegalStateException exception) {
            log.info(exception.getMessage());
        }
    }
}
