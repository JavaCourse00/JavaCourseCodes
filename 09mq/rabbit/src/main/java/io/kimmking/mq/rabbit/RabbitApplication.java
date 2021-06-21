package io.kimmking.mq.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitApplication.class, args);
    }

    @Autowired
    MessageProducer producer;

    @Bean
    ApplicationRunner run() {
        return args -> {
            for (int i = 0; i < 1000; i++) {
                producer.sendMessage(i+" message by cuicuilaoshi.");
            }
        };
    }

}
