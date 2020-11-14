package io.kimmking.springboot01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms    //启动消息队列
@EnableMongoRepositories
public class Springboot01Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot01Application.class, args);
	}

}
