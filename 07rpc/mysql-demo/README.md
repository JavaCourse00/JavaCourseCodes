## 说明

第七周-作业2. （必做）: 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

## 操作步骤


### 第七周-作业2. （必做）


#### 1. 创建基本项目

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


#### 2. 准备mysql环境

mysql服务器, 可以使用这些方式:

- 物理机安装
- 虚拟机安装
- 购买云服务
- 使用已有MySQL
- 使用Docker
- 使用docker-compose: 参考配置文件: [docker-compose.yml](./docker-compose.yml)

这里使用 docker-compose 方式。

根据配置信息, 我们需要创建好以下2个目录:

- `./docker_data/docker-entrypoint-initdb.d` ; MySQL镜像启动时会自动执行下面的SQL.
- `./docker_data/var/lib/mysql` ; 注意这个目录下不能有任何文件, 否则启动报错。

安装好 docker 之后, 使用 `docker-compose up -d` 命令来启动。

这个命令会自动查找当前目录下的 [docker-compose.yml](./docker-compose.yml) 配置文件。

如果启动失败, 可以在当前目录下, 使用 `docker-compose  logs -f` 命令查看日志信息。

可以使用命令来查看当前物理机监听的端口号;


```shell
# mac
lsof -iTCP -sTCP:LISTEN -n -P

# linux
netstat -ntlp

```

启动成功后, 应该能看到我们配置的 `13306` 端口信息.

然后使用MySQL客户端连接即可, 我们在配置文件里面指定了ROOT用户的密码为 `123456`;


#### 3. 项目基础配置

如果直接启动下载下来的 MysqlDemoApplication, 报错信息大致如下:

```text
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 
  'url' attribute is not specified 
   and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class
```

可以看到这里提示 缺少了DataSource的url等配置信息

我们增加一个配置文件: [src/main/resources/application.yml](./src/main/resources/application.yml)

文件内容参考:

```yaml
# Spring相关配置
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      minimum-idle: 1
      maximum-pool-size: 5
      auto-commit: true
      idle-timeout: 30000
      pool-name: MySQLHikariCP1
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:13306/mysql_demo?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: 123456

```

然后再增加 logback的日志级别:

```yaml
#  Logback日志级别配置
logging.level.root: info
logging.level.com.cncounter: debug
logging.level.org.springframework.test.web.servlet.result: debug

```

以及 Tomcat 配置:

```yaml
# Tomcat 配置
server:
  port: 8080
  tomcat:
    uri-encoding: UTF-8
    max-threads: 1000
    min-spare-threads: 10
  servlet:
    context-path: /
    encoding:
      enabled: true
      force: true
      charset: UTF-8
```


然后重新启动 MysqlDemoApplication.

启动成功之后, 可以查看本机的端口号, 也可以访问: [http://localhost:8080/](http://localhost:8080/)


提示: 可以用 jvisualvm 来观察启动好的Java进程。


#### 4. 数据库脚本

可以使用前面课程的作业中提交的数据库表。

也可以参考: [docker_data/docker-entrypoint-initdb.d/01-mysql_demo-init.ddl.sql](./docker_data/docker-entrypoint-initdb.d/01-mysql_demo-init.ddl.sql)


#### 5. MyBatis生成器与配置

官方文档: [MyBatis Generator Doc](https://mybatis.org/generator/configreference/xmlconfig.html)

配置文件: [src/main/resources/MybatisGeneratorConfig.xml](./src/main/resources/MybatisGeneratorConfig.xml)

在 [pom.xml](./pom.xml) 文件中增加 `mybatis-generator-maven-plugin` 插件.

执行生成（也可以使用  [generateMapper.sh](./generateMapper.sh)  脚本文件）:

```shell
mvn mybatis-generator:generate -X -e
```

可以看到生成了以下文件:

- [`src/main/resources/com/cncounter/mysqldemo/dao/mapper/TBizOrderMapper.xml`](./src/main/resources/com/cncounter/mysqldemo/dao/mapper/TBizOrderMapper.xml)
- [`src/main/java/com/cncounter/mysqldemo/dao/mapper/TBizOrderMapper.java`](./src/main/java/com/cncounter/mysqldemo/dao/mapper/TBizOrderMapper.java)
- [`src/main/java/com/cncounter/mysqldemo/model/TBizOrder.java`](./src/main/java/com/cncounter/mysqldemo/model/TBizOrder.java)

#### MyBatis 扫描配置

要使用MyBatis, 首先要配置数据源. 当然, 在前面的步骤中我们已经配置好了. 

文件内容参考: [src/main/resources/application.yml](./src/main/resources/application.yml)

然后, 我们在配置文件中增加 MyBatis 相关的配置



#### WebMVC






#### 请求链路分析






#### MySQL服务器参数调整





#### 应用程序优化