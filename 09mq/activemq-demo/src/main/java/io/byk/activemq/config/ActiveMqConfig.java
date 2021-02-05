package io.byk.activemq.config;

/**
 * 常量类
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
public class ActiveMqConfig {
    // 测试队列
    public static final String ACTIVE_MQ_QUEUE = "test.queue";
    // 测试主题
    public static final String ACTIVE_MQ_TOPIC = "test.topic";
    // 目标地址，61616 端口为 JMS 协议，具体查看在 apache-activemq-5.16.1/conf/activemq.xml
}
