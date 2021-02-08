
## Week13 周四作业：
### 1. [（必做）搭建 ActiveMQ 服务，基于 JMS，写代码分别实现对于 queue 和 topic 的消息生产和消费，代码提交到 github。](src/main/java/io/byk/activemq/jms)

使用`JmsMessagingTemplate`模板类来实现

### 2. [（选做）基于数据库的订单表，模拟消息队列处理订单](src/main/java/io/byk/queue/order)

> - 一个程序往表里写新订单，标记状态为未处理 (status=0);
> - 另一个程序每隔 100ms 定时从表里读取所有 status=0 的订单，打印一下订单数据，然后改成完成 status=1；
> - （挑战☆）考虑失败重试策略，考虑多个消费程序如何协作。

#### 解决org.apache.ibatis.binding.BindingException: Invalid bound statement (not found)问题

在`application.yml`文件下添加配置信息：

```sh
mybatis:
  mapper-locations: classpath:mapper/*.xml
```

#### ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 

建议不要在创建表的过程中使用mysql保留字，避免后期造成麻烦

### 3.[（选做）将上述订单处理场景，改成使用 ActiveMQ 发送消息处理模式。](src/main/java/io/byk/queue/order)

#### ActiveMQ序列化异常Forbidden class ! This class is not trusted to be serialized as ObjectMessage payload

在`application.yml`文件下添加配置信息：

```
spring:
  activemq:
    broker-url: tcp://127.0.0.1:61616 # 目标地址，61616 端口为 JMS 协议，具体查看在 apache-activemq-5.16.1/conf/activemq.xml
    user: admin
    password: admin
    pool:
      enabled: false
    packages:
      trust-all: true
```