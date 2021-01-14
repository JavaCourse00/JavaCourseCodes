package io.kimmking.mq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.QUEUE_B)
@Slf4j
public class MsgReceiverB {

    @RabbitHandler
    public void process(String content) {
        log.info("接收处理队列B当中的消息： " + content);
    }

}