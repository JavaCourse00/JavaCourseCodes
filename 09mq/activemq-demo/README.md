
## Week13 周四作业：
### 1. [（必做）搭建 ActiveMQ 服务，基于 JMS，写代码分别实现对于 queue 和 topic 的消息生产和消费，代码提交到 github。](src/main/java/io/byk/activemq/jms)

使用`JmsMessagingTemplate`模板类来实现

## 2. [（选做）基于数据库的订单表，模拟消息队列处理订单](src/main/java/io/byk/queue/order)

> - 一个程序往表里写新订单，标记状态为未处理 (status=0);
> - 另一个程序每隔 100ms 定时从表里读取所有 status=0 的订单，打印一下订单数据，然后改成完成 status=1；
> - **TODO （挑战☆）考虑失败重试策略，考虑多个消费程序如何协作。**

