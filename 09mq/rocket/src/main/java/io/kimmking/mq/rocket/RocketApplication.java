package io.kimmking.mq.rocket;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class RocketApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void run(String... args) throws Exception {

        String topic = "test-k1";
        SendResult sendResult = rocketMQTemplate.syncSend(topic, "Hello, World!" + System.currentTimeMillis());
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", topic, sendResult);

        sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(
                new Order(System.currentTimeMillis(),"CNY2USD", 0.1501d))
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build());
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", topic, sendResult);

        String topic1 = "test-k2";
        rocketMQTemplate.asyncSend(topic1, new Order(System.currentTimeMillis(),"CNY2USD", 0.1502d), new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                System.out.printf("async onSucess SendResult=%s %n", result);
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.printf("async onException Throwable=%s %n", throwable);
            }

        });


    }

}
