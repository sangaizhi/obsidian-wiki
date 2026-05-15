### Spring 的前世今生
    V.1.X : 注解驱动启蒙时代
    V.2.0 : 注解驱动过渡时代
    V.2.5 : 引入了新的骨架式 Annotation
    V.3.X : 注解驱动黄金时代
    V.4.X : 注解驱动完善时代
    V.5.X : 注解驱动成熟时代 

    Spring 的价值：简化开发，聚焦业务。
    Spring 简化开发的四个基本策略：
        1)、基于 POJO 的轻量级和最小侵入性编程
        2)、通过依赖注入和面向接口松耦合
        3)、基于切面和惯性(编程习惯)进行声明式编程
        4)、通过切面和模板减少样板式代码

### Spring 的编程思想
#### OOP 
    Object Oriented Programming 面向对象编程，用程序归纳总结生活中一切事物。一句话归纳：封装、继承、多态。
#### BOP
    Bean Oriented Programming 面向 Bean 编程，面向 Bean(普通的 Java 类)设计程序，解放程序员。一句话归纳：一切从 Bean 开始。
#### AOP
    Aspect Oriented Programming 面向切面编程，找出多个类中有一定规律的代码，开发时拆分，运行时再合并。面向切面编程就是面向规则编程。一句话归纳：解耦，专人做专事。
#### IOC
    Inversion of control 控制反转，将 new 对象的动作交给 Spring 管理，并由 Spring保存已创建的对象( IOC 容器)。一句话归纳：转交控制权(控制权反转)。
#### DI/DL
    Dependency Injection 依赖注入或者 Dependency Lookup 依赖查找，Spring不仅保存自己创建的对象，而且保存对象与对象之间的关系。注入即赋值，主要三种方式：构造方法、set方法、直接赋值。一句话归纳：自动赋值

### Spring 架构
#### Spring5 模块结构图
![avatar](https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/spring/Spring5ModuleStructure.png)
#### Spring 个模块依赖
![avatar](https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/spring/Spring5ModuleDependency.png)

#### Spring 核心模块
    1、spring-core
        依赖注入 IoC 与 DI 的基本实现
    2、spring-beans
        Bean 工厂与 Bean 的装配
    3、spring-context
        定义基础的 Spring 的 Context 上下文，即 IoC 容器
    4、spring-context-support
        对 Spring IoC 容器的扩展支持，以及 IoC 子容器
    5、spring-context-indexer
        Spring 的类管理组件和 Classpath 扫描
    6、spring-expression     
        Spring 表达式语言

#### Spring 切面编程模块        
    1、spring-aop
        面向切面编程的应用模块，整合 ASM、CGLib、JDKProxy    
    2、spring-aspects 
        集成 AspecJ，AOP 应用框架
    3、spring-instrument     
        动态 Class Loading 模块

#### Spring 数据访问与集成模块
    1、spring-jdbc
        Spring 提供的 JDBC 抽象框架的主要实现模块，用于简化 Spring JDBC 操作
    2、spring-tx
        Spring JDBC 事物控制实现模块
    3、spring-orm
        主要集成 Hibernate、Java Persistence API(JPA) 和 Java Data Objects (JDO)
    4、spring-oxm
        将 Java 对象映射成 XML 数据，或者将 XML 数据映射成 Java 对象
    5、spring-jms
        Java Messaging Service 能够发送和接收消息

#### Spring Web 模块
    1、spring-web
        提供了最基础的 web 支持，主要建立于核心容器之上，通过 Servlet 或者 Listeners 来初始化 IoC 容器
    2、spring-webmvc
        实现了 Spring MVC 的 web 应用
    3、spring-websocket
        主要是于 web 前端的全双工通讯的协议
    4、spring-webflux        
        一个新的非堵塞函数式 Reactive Web 框架，可以用来建立异步的、非阻塞、事件驱动服务
        
#### Spring 通讯报文模块
    1、spring-messaging
        从 Spring4 开始加入的一个模块，主要职责是为 Spring框架集成一些基础的报文传送应用
        
#### Spring 集成测试模块
    1、spring-test
        主要为测试提供支持
           
#### Spring 集成兼容模块
    1、spring-framework-bom
        Bill of Materials 解决 Spring 的不同模块依赖版本不同的问题    

### Spring 版本命名规则
    1、Snapshot 快照版
        尚不稳定，尚处于开发中的版本
    2、Release 稳定版
        功能相对稳定，可以对外发行，但有时间限制
    3、RC 终测版
        Release Candidate(最终测试),即将作为正式版发布。        
    4、GA 正式版
        代表广泛可用的稳定版( General Availability)
    5、M 里程碑版
        具有一些权限的功能或是具有里程碑意义的版本
        
