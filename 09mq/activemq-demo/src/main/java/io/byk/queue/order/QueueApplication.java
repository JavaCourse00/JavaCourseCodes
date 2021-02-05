package io.byk.queue.order;

import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.common.collect.Lists;

import io.byk.activemq.jms.JmsActiveMqApplication;
import io.byk.queue.order.entity.Order;
import io.byk.queue.order.service.QueueConsumer;
import io.byk.queue.order.service.QueueProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@SpringBootApplication
@Slf4j
public class QueueApplication implements ApplicationRunner {
    private final static Queue<Order> orderQueue = Lists.newLinkedList();

    @Resource
    QueueProducer queueProducer;

    @Resource
    QueueConsumer queueConsumer;

    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 10; i++) {
            log.info(queueProducer.sendMessage(orderQueue) + "<=======入队");
        }
        while (true) {
            try {
                log.info(queueConsumer.receiveMessage(orderQueue) + "=======>出队");
                Thread.sleep(100);
            } catch (IllegalStateException exception) {
                log.info(exception.getMessage());
                break;
            }
        }
    }
}
