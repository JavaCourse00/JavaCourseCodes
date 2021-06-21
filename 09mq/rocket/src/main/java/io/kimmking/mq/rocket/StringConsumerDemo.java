package io.kimmking.mq.rocket;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "test1", topic = "test-k1")
public class StringConsumerDemo implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) { // one way
        System.out.println(this.getClass().getName() + " -> " + message);
    }
}
