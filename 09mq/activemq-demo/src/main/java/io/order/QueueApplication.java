package io.order;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 1; i <= 10; i++) {
            log.info(queueProducer.sendMessage() + "<=======入队");
        }
        while (true) {
            try {
                queueConsumer.receiveMessage();
                Thread.sleep(100);
            } catch (IllegalAccessException exception) {
                log.info(exception.getMessage());
            } catch (IllegalStateException exception){
                log.info(exception.getMessage());
                break;
            }
        }
    }
}
