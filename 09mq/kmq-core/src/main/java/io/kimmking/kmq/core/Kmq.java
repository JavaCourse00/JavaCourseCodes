package io.kimmking.kmq.core;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class Kmq {

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue(capacity);
    }

//    public List<KmqConsumer> consumers = new ArrayList<>();

    private List<MessageListener> listeners = new ArrayList<>();

    private final String topic;

    private final int capacity;

    private LinkedBlockingQueue<KmqMessage> queue;

    public boolean send(KmqMessage message) {
        boolean offered = queue.offer(message);
        if(offered) {
            listeners.forEach(listener -> {
                try {
                    listener.onMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return offered;
    }

    public KmqMessage poll() {
        return queue.poll();
    }

    @SneakyThrows
    public KmqMessage poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

}
