package io.kimmking.mq.rocket;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "test2", topic = "test-k2")
public class OrderConsumerDemo implements RocketMQReplyListener<Order,String> {

    @Override
    public String onMessage(Order order) {
        System.out.println(this.getClass().getName() + " -> " + order);
        return "Process&Return [" + order + "].";
    }
}
