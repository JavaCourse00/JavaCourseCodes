package io.kimmking.javacourse.kafka.kimmking;

import io.kimmking.javacourse.kafka.Order;

import com.alibaba.fastjson.JSON;
import kafka.common.KafkaException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerImpl implements Producer {
    private Properties properties;
    private KafkaProducer<String, String> producer;
    private final String topic = "order-test1";

    public ProducerImpl() {
        properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
//        properties.put("queue.enqueue.timeout.ms", -1);
//        properties.put("enable.idempotence", true);
//        properties.put("transactional.id", "transactional_1");
//        properties.put("acks", "all");
//        properties.put("retries", "3");
//        properties.put("max.in.flight.requests.per.connection", 1);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(properties);
        //producer.initTransactions();
    }

    @Override
    public void send(Order order) {
        try {
            //producer.beginTransaction();
            ProducerRecord record = new ProducerRecord(topic, order.getId().toString(), JSON.toJSONString(order));
            producer.send(record, (metadata, exception) -> {
//                if (exception != null) {
//                    producer.abortTransaction();
//                    throw new KafkaException(exception.getMessage() + " , data: " + record);
//                }
            });
            //producer.commitTransaction();

        } catch (Throwable e) {
            //producer.abortTransaction();
        }
        //System.out.println("************" + json + "************");
    }

    @Override
    public void close() {
        producer.close();
    }
}