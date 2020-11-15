# 第2周作业


## 作业内容

> Week02 作业题目（周四）：

1.使用 GCLogAnalysis.java 自己演练一遍串行 / 并行 /CMS/G1 的案例。
2.使用压测工具（wrk 或 sb），演练 gateway-server-0.0.1-SNAPSHOT.jar 示例。
3.（选做） 如果自己本地有可以运行的项目，可以按照 2 的方式进行演练。
4.（必做） 根据上述自己对于 1 和 2 的演示，写一段对于不同 GC 的总结，提交到 Github。

> Week02 作业题目（周六）：

1.（选做）运行课上的例子，以及 Netty 的例子，分析相关现象。

2.（必做）写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 Github。


## 操作步骤


### 第二周-周六-作业2

1. 打开 Spring 官网: https://spring.io/
2. 找到 Projects --> Spring Initializr:  https://start.spring.io/
3. 填写项目信息, 生成 maven 项目; 下载并解压。
4. Idea或者Eclipse从已有的Source导入Maven项目。
5. 搜索依赖， 推荐 mvnrepository: https://mvnrepository.com/
6. 搜索 OkHttp 或者 HttpClient，然后在 pom.xml 之中增加对应的依赖。
7. 使用OkHttp
  - 7.1 查找OkHttp官网: https://square.github.io/okhttp/
  - 7.2 参照官方示例编写代码: [OkHttpUtils.java](https://github.com/renfufei/JAVA-000/blob/main/Week_02/homework02/src/main/java/com/renfufei/homework02/OkHttpUtils.java)
8. 使用HttpClient
  - 8.1 查找官网: http://hc.apache.org/
  - 8.2 参照官方示例编写代码: [HttpClientHelper.java](https://github.com/renfufei/JAVA-000/blob/main/Week_02/homework02/src/main/java/com/renfufei/homework02/HttpClientHelper.java)
  - 8.3 执行如果报错, 根据提示，增加 commons-logging 或者其他日志依赖。
9. 执行与测试.
