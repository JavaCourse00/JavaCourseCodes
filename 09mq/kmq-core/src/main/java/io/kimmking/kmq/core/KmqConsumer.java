package io.kimmking.kmq.core;

import lombok.Getter;

public class KmqConsumer<T> {

    private final KmqBroker broker;

    @Getter
    private final String id;

    private Kmq kmq;

    public KmqConsumer(String id, KmqBroker broker) {
        this.id = id;
        this.broker = broker;
    }

    public void subscribe(String topic) {
        this.kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public void subscribe(String topic, MessageListener listener) {
        this.kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        this.kmq.addListener(listener);
    }

    public KmqMessage<T> poll(long timeout) {
        return kmq.poll(timeout);
    }

}
