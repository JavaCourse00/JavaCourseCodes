package io.byk.activemq.jms;


import static io.byk.config.ActiveMqConfig.ACTIVE_MQ_QUEUE;
import static io.byk.config.ActiveMqConfig.ACTIVE_MQ_TOPIC;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.byk.activemq.jms.queue.QueueProducer;
import io.byk.activemq.jms.topic.TopicPublisher;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
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
        // 测试队列
        for (int i = 0; i < 10; i++) {
            String message = "队列消息" + i;
            queueProducer.sendMessage(ACTIVE_MQ_QUEUE, message);
        }
        // 测试主题
        for (int i = 0; i < 10; i++) {
            String message = "主题消息" + i;
            topicPublisher.sendMessage(ACTIVE_MQ_TOPIC, message);
        }
    }
}
