package io.byk.activemq;


import static io.byk.activemq.config.ActiveMqConfig.ACTIVE_MQ_QUEUE;

import java.util.Queue;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.byk.activemq.queue.QueueProducer;
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

    public static void main(String[] args) {
        SpringApplication.run(JmsActiveMqApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 10; i++) {
            String message;
            log.info(message = "生产消息" + i);
            queueProducer.sendMessage(ACTIVE_MQ_QUEUE, message);
        }
    }
}
