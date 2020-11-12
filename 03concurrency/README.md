# 第3周作业


## 作业内容

Week03 作业题目（周四）：

基础代码可以 fork： https://github.com/JavaCourse00/JavaCourseCodes

02nio/nio02 文件夹下

实现以后，代码提交到 Github。

1.（必做）整合你上次作业的 httpclient/okhttp；

2.（选做）使用 netty 实现后端 http 访问（代替上一步骤）

Week03 作业题目（周六）：

1.（必做）实现过滤器。
2.（选做）实现路由。

作业提交规范：

1. 作业不要打包 ；
2. 同学们写在 md 文件里，而不要发 Word, Excel , PDF 等 ；
3. 代码类作业需提交完整 Java 代码，不能是片段；
4. 作业按课时分目录，仅上传作业相关，笔记分开记录；
5. 画图类作业提交可直接打开的图片或 md，手画的图手机拍照上传后太大，难以查看，推荐画图（推荐 PPT、Keynote）；
6. 提交记录最好要标明明确的含义（比如第几题作业）。

## 操作步骤

### 1.（必做）整合你上次作业的 httpclient/okhttp

1. 下载老师的项目: https://github.com/JavaCourse00/JavaCourseCodes
2. 解压, 拷贝其中 `02nio` 路径下的 `nio02` 目录到第三周的作业目录下。
3. Idea或者Eclipse打开这个Maven项目。
4. 增加自己的包名, 以做标识。
5. 将第二周的作业代码整合进来: [homework02](../Week_02/homework02/) 中的代码和pom.xml依赖。
6. 将 nio01 中的 HttpServer03 代码整合进来作为后端服务，改名为 [BackendServer](https://github.com/renfufei/JAVA-000/blob/main/Week_03/nio02/src/main/java/com/renfufei/homework03/BackendServer.java), 监听 8088 端口。
7. 找到Netty官网: https://netty.io/wiki/user-guide-for-4.x.html
8. 参照官方示例, 编写自己的过滤器 [ProxyBizFilter](https://github.com/renfufei/JAVA-000/blob/main/Week_03/nio02/src/main/java/com/renfufei/homework03/ProxyBizFilter.java);
9. 可以加入到 [HttpInboundHandler.java](https://github.com/renfufei/JAVA-000/blob/main/Week_03/nio02/src/main/java/io/github/kimmking/gateway/inbound/); 【实际上应该加入到 [HttpInboundInitializer](./nio02/src/main/java/io/github/kimmking/gateway/inbound/HttpInboundInitializer.java) 的初始化方法中】。
10. 修改 [HttpOutboundHandler](https://github.com/renfufei/JAVA-000/blob/main/Week_03/nio02/src/main/java/io/github/kimmking/gateway/outbound/httpclient4/HttpOutboundHandler.java) 类，集成自己写的第二周的作业代码；
