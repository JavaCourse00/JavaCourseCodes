package io.kimmking.javacourse.kafka;

import io.kimmking.javacourse.kafka.kimmking.ConsumerImpl;
import io.kimmking.javacourse.kafka.kimmking.ProducerImpl;

public class KafkaProducerDemo {

    public static void main(String[] args) {
        testProducer();
    }

    private static void testProducer() {
        ProducerImpl producer = new ProducerImpl();
        for (int i = 0; i < 1000; i++) {
            producer.send(new Order(1000L + i,System.currentTimeMillis(),"USD2CNY", 6.5d));
            producer.send(new Order(2000L + i,System.currentTimeMillis(),"USD2CNY", 6.51d));
        }
    }
}
