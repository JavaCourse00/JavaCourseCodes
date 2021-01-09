package io.kimmking.javacourse.kafka.kimmking;

import io.kimmking.javacourse.kafka.Order;

public interface Producer {

    void send(Order order);

    void close();

    // add your interface method here

    // and then implement it

}
