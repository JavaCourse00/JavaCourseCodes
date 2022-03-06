# 第7周作业


## 作业内容

> Week07 作业题目：

###Week07 作业题目

1. （选做）用今天课上学习的知识，分析自己系统的 SQL 和表结构
2. （必做）按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率
3. （选做）按自己设计的表结构，插入 1000 万订单模拟数据，测试不同方式的插入效率
4. （选做）使用不同的索引或组合，测试不同方式查询效率
5. （选做）调整测试数据，使得数据尽量均匀，模拟 1 年时间内的交易，计算一年的销售报表：销售总额，订单数，客单价，每月销售量，前十的商品等等（可以自己设计更多指标）
6. （选做）尝试自己做一个 ID 生成器（可以模拟 Seq 或 Snowflake）
7. （选做）尝试实现或改造一个非精确分页的程序
8. （选做）配置一遍异步复制，半同步复制、组复制
9. （必做）读写分离 - 动态切换数据源版本 1.0
10. （必做）读写分离 - 数据库框架版本 2.0
11. （选做）读写分离 - 数据库中间件版本 3.0
12. （选做）配置 MHA，模拟 master 宕机
13. （选做）配置 MGR，模拟 master 宕机
14. （选做）配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构

### 作业提交规范：

1. 作业不要打包 ；
2. 同学们写在 md 文件里，而不要发 Word, Excel , PDF 等 ；
3. 代码类作业需提交完整 Java 代码，不能是片段；
4. 作业按课时分目录，仅上传作业相关，笔记分开记录；
5. 画图类作业提交可直接打开的图片或 md，手画的图手机拍照上传后太大，难以查看，推荐画图（推荐 PPT、Keynote）；
6. 提交记录最好要标明明确的含义（比如第几题作业）。



## 操作步骤


### 第七周-作业1. （选做）

> 本题目需要同学们自己完成.

1. 找一个业务系统。
2. 分析SQL表结构
3. 范式
4. 考虑: 开发效率、查询效率、拓展性、索引、有哪些优化空间。
5. 其他


### 第七周-作业2. （必做）

0. 作业题目: 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率
1. 打开 Spring 官网: https://spring.io/
2. 找到 Projects --> Spring Initializr:  https://start.spring.io/
3. 填写项目信息:
  * Project: Maven Project
  * Language: Java
  * Spring Boot版本: 2.5.4
  * Group: 自己的包名, 以做标识;
  * Artifact: mysql-demo
  * JDK版本: 8
  * Dependencies: 添加依赖, 比如 MyBatis, Spring Web, MySQL, JDBC
  * 生成 maven 项目; 下载并解压。 参考: [mysql-demo项目Share信息](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.5.4&packaging=jar&jvmVersion=1.8&groupId=com.cncounter&artifactId=mysql-demo&name=mysql-demo&description=MySQL%20Demo&packageName=com.cncounter.mysql-demo&dependencies=mybatis,web,mysql,data-jdbc)

4. Idea或者Eclipse从已有的Source导入Maven项目。
5. 搜索依赖， 推荐 mvnrepository: https://mvnrepository.com/
6. 搜索 fastjson , 然后在 pom.xml 之中增加对应的依赖。
7. 准备MySQL环境, 创建订单表
  - 7.1
8. 生成项目;
  - 8.1 创建Controller
9. 执行与测试.
