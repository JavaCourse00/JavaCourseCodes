package io.kimmking.kmq.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KmqMessage<T> {

    static AtomicLong MID = new AtomicLong(0);

    private HashMap<String,Object> headers = new HashMap<>();
    private String topic;
    private Long id;
    private T body;

    public KmqMessage(String topic, T body) {
        this.topic = topic;
        this.body = body;
        this.id = MID.getAndIncrement();
    }

    public static <T> KmqMessage<T> from(String topic, T body) {
        return new KmqMessage<>(topic, body);
    }

}
