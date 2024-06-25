package io.kimmking.kmq.core;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/13 下午3:29
 */
public interface MessageListener {

    void onMessage(KmqMessage message) throws Exception;

}
