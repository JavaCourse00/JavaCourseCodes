package io.kimmking.mq.pulsar;

import lombok.SneakyThrows;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.Schema;
import org.springframework.stereotype.Component;

@Component
public class ProducerDemo {
    Producer<String> stringProducer;

    @SneakyThrows
    public ProducerDemo(){
        stringProducer = Config.createClient().newProducer(Schema.STRING)
                .topic("my-topic")
                .create();
    }

    @SneakyThrows
    public void sendMsg() {
        for (int i = 0; i < 1000; i++) {
            stringProducer.send(i + " message from pulsar.");
        }
    }


//    //关闭操作也可以是异步的：
//
//    producer.closeAsync()
//        .thenRun(() -> System.out.println("Producer closed"));
//        .exceptionally((ex) -> {
//          System.err.println("Failed to close producer: " + ex);
//        return ex;
//    });

    // 控制发送行为
//    Producer<byte[]> producer = client.newProducer()
//            .topic("my-topic")
//            .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
//            .sendTimeout(10, TimeUnit.SECONDS)
//            .blockIfQueueFull(true)
//            .create();


    //异步发送
//    producer.sendAsync("my-async-message".getBytes()).thenAccept(msgId -> {
//        System.out.printf("Message with ID %s successfully sent", msgId);
//    });


    //   添加参数
//    producer.newMessage()
//            .key("my-message-key")
//            .value("my-async-message".getBytes())
//            .property("my-key", "my-value")
//            .property("my-other-key", "my-other-value")
//            .send();


}
