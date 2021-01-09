# 消息队列培训-Kafka进阶

## 作业：具体场景下的Kafka接口设计实现

### 背景
请各位同学结合定序 & 撮合的业务特点, 练习 Producer + Consuer 的用法：
- 定序要求: 数据有序并且不丢失
- 撮合要求: 可重放, 顺序消费, 消息仅处理一次

### 消息格式
消息的内容格式为：// 见本仓库的代码
```
public class Order{
private Long id；
private Long ts;
private String symbol;
private Double price;

// 省略setter getter...
}
```

### 作业要求
1. `Producer`接口应该有序发送消息
2. `Producer`接口应该不丢失消息
3. `Consumer`接口应该顺序消费消息
4. `Consumer`接口应该支持从offset重新接收消息
5. `Consumer`接口应该支持消息的幂等处理，即根据id去重
6. 设计挑战：尝试接口对外暴露的使用方式，完全屏蔽了kafka的原生接口和类
7. 编码挑战：实现程序都正确，而且写了单元测试

### 提交方式：过程分（3分）
1. 设计一个 `io.kimmking.javacourse.自己姓名拼音.Producer` 接口，需满足以上要求，并尝试实现
2. 设计一个 `io.kimmking.javacourse.自己姓名拼音.Consumer` 接口，需满足以上要求，并尝试实现
3. 提交以上代码，并提交一个Pull Request到本仓库，在钉钉群回复已经完成，1分


