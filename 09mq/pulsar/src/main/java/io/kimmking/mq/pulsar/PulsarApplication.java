package io.kimmking.mq.pulsar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PulsarApplication {

    public static void main(String[] args) {
        SpringApplication.run(PulsarApplication.class, args);
    }


    @Autowired
    ProducerDemo producer;

    @Autowired
    ConsumerDemo consumer;

    @Bean
    ApplicationRunner run() {
        return args -> {
            new Thread(() -> {
                consumer.consume();
            }).start();

            producer.sendMsg();

        };
    }


}
