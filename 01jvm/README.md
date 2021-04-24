# 第1周作业


参见 我的教室 -> 本周作业

## 作业内容


> Week01 作业题目：

1.（选做）自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码，有问题群里讨论。

2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。

3.（必做）画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系。

4.（选做）检查一下自己维护的业务系统的 JVM 参数配置，用 jstat 和 jstack、jmap 查看一下详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。

注意：如果没有线上系统，可以自己 run 一个 web/java 项目。

> Week01 作业题目:

1.（选做）本机使用 G1 GC 启动一个程序，仿照课上案例分析一下 JVM 情况。


## 操作步骤


### 作业1（选做）

1. 编写代码, 根据自己的意愿随意编写, 可参考: [Hello.java](./Hello.java)
2. 编译代码, 执行命令： `javac -g Hello.java`
3. 查看反编译的代码。
  - 3.1 可以安装并使用idea的jclasslib插件, 选中 [Hello.java](./Hello.java) 文件, 选择 `View --> Show Bytecode With jclasslib` 即可。
  - 3.2 或者直接通过命令行工具 javap, 执行命令: `javap -v Hello.class`
4. 分析相关的字节码。【此步骤需要各位同学自己进行分析】


### 作业2（必做）

1. 打开 Spring 官网: https://spring.io/
2. 找到 Projects --> Spring Initializr:  https://start.spring.io/
3. 填写项目信息, 生成 maven 项目; 下载并解压。
4. Idea或者Eclipse从已有的Source导入Maven项目。
5. 从课件资料中找到资源 Hello.xlass 文件并复制到 src/main/resources 目录。
6. 编写代码，实现 findClass 方法，以及对应的解码方法
7. 编写main方法，调用 loadClass 方法；
8. 创建实例，以及调用方法
9. 执行.

具体代码可参考: [XlassLoader.java](./XlassLoader.java)
