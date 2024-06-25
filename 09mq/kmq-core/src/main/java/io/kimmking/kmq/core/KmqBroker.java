package io.kimmking.kmq.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class KmqBroker { // Broker+Connection

    public static final int CAPACITY = 10000;

    private final Map<String, Kmq> kmqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name){
        kmqMap.putIfAbsent(name, new Kmq(name,CAPACITY));
    }

    public Kmq findKmq(String topic) {
        return this.kmqMap.get(topic);
    }

    public KmqProducer createProducer() {
        return new KmqProducer(this);
    }

    final AtomicInteger consumerId = new AtomicInteger(0);
    public KmqConsumer createConsumer() {
        return new KmqConsumer("CID" + consumerId.getAndIncrement(), this);
    }

}
