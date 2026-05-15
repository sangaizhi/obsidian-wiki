## IoC 中3个最重要的类
              BeanDefinition <---------------------------
                     |                                   |
                     |                                   |
                     ↓                                   |
        --------- BeanWapper                             |
        |                                                |
        |                                                |
        ↓                                                |
    getBean() ----> ApplicationContext ------>  BeanDefinitionReader

## Spring IoC 和 DI 的基本执行顺序   

1、调用 Servlet 的 init() 方法；    
2、读取配置文件，不管读取的是 properties、xml、yml 任何格式的配置文件，读取到内存里都会定义为 BeanDefinition.
BeanDefinition 是由 BeanDefinitionReader 读取配置文件创建的。    

3、扫描相关的类    
4、初始化 IoC 容器，并且实例化对象;为了对象能够很好的扩展（后续可能会变成代理对象），Spring 中使用了装饰器模式。为了保持代理对象与原先对象之间的关联关系，
所以 Spring 使用了一个 BeanWrapper，BeanWrapper 保存可原先对象与未来各种对象之间的关联关系。BeanWrapper 是在 getBean() 的时候创建的。    
5、完成 DI 注入    
6、Handle  Mapping    

    ApplicationContext:可以简单的理解为它是一个工厂类，它有一个 getBean() 方法，用来从 IoC 容器中去获取一个实例的方法，并且默认是单例、延迟加载的。

    DI 是在 初始化以后发生的。Spring 中 DI 是有 getBean() 触发的：
    1、调用 getBean() 创建对象    
    2、立即发生 DI

    在调用 getBean() 之前就需要有 ApplicationContext，所以在调用 Servlet 的 init() 方法时，就需要初始化化 ApplicationContext。
