package io.byk.activemq;


import static io.byk.activemq.config.ActiveMqConfig.ACTIVE_MQ_QUEUE;
import static io.byk.activemq.config.ActiveMqConfig.ACTIVE_MQ_TOPIC;

import java.util.Queue;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.byk.activemq.queue.QueueProducer;
import io.byk.activemq.topic.TopicPublisher;
import lombok.extern.slf4j.Slf4j;

/**
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@SpringBootApplication
@Slf4j
public class JmsActiveMqApplication implements ApplicationRunner {
    @Resource
    private QueueProducer queueProducer;
    @Resource
    private TopicPublisher topicPublisher;

    public static void main(String[] args) {
        SpringApplication.run(JmsActiveMqApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 10; i++) {
            String message = "队列消息" + i;
            queueProducer.sendMessage(ACTIVE_MQ_QUEUE, message);
        }

        for (int i = 0; i < 10; i++) {
            String message = "主题消息" + i;
            topicPublisher.sendMessage(ACTIVE_MQ_TOPIC, message);
        }
    }
}
