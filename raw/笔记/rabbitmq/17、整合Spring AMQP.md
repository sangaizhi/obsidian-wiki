### RabbitAdmin
    RabbitAdmin 类可以甚好的操作RabbitMQ，在 Spring 中直接进行注入，
    注意： autoStartup  必须要设置为 true, 否则 Spring 容器不会加载 RabbitMQ类；
    RabbitAdmin的底层实现就是从 Spring 容器中获取 Exchange、Binding、RoutingKey 及 Queue 的 @Bean 声明。 然后使用 
    RabbitTemplate 的 execute 方法执行对应的声明、修改、删除等一系列 RabbitMQ 基础功能操作。
  测试代码见 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/blob/master/rabbitmq-spring/src/test/java/imooc/rabbitmq/spring/RabbitmqSpringApplicationTests.java#testRabbitAdmin)  testRabbitAdmin() 方法。   
### 源码分析
#### RabbitMQ 是如何创建的
    从 RabbitAdmin 类的声明上可以看出实现了 `InitializingBean` 接口，并且重写了 `afterPropertiesSet()` 方法，`afterPropertiesSet()`
    就是在创建完 RabbitAdmin 对象后执行的操作。下面使用代码的方式分析 RabbitAdmin 如何从 Spring 的 IOC 容器中获取 Exchange、
    Binding、RoutingKey 及 Queue 。
```java
public void afterPropertiesSet() {
    // 同步阻塞执行
    synchronized (this.lifecycleMonitor) {
        // 1. 判断 Rabbitmin 是否已经初始化并且是立即启动的    
        if (this.running || !this.autoStartup) {
            return;
        }

        // 省略代码
        ···  
        
        // 2.添加一个 ConnectionListener
        this.connectionFactory.addConnectionListener(new ConnectionListener() {
            // 防止栈溢出
            private final AtomicBoolean initializing = new AtomicBoolean(false);
            @Override
            public void onCreate(Connection connection) {
                if (!initializing.compareAndSet(false, true)) {
                    // If we are already initializing, we don't need to do it again...
                    return;
                }
                try {
                    if (RabbitAdmin.this.retryTemplate != null) {
                        // 省略代码
                        ···  
                    }
                    else {
                        // 3. 执行初始化方法,在该方法中获取所有声明的 exchange、queue、binding 
                        initialize();
                    }
                }
                finally {
                    initializing.compareAndSet(true, false);
                }
            }
            @Override
            public void onClose(Connection connection) {
            }
        });
        this.running = true;
    }
}

/**
* 执行初始化方法
 */	
public void initialize() {

    if (this.applicationContext == null) {
        this.logger.debug("no ApplicationContext has been set, cannot auto-declare Exchanges, Queues, and Bindings");
        return;
    }
    // 1. 从 Spring 容器中获取所有的 Exchange
    Collection<Exchange> contextExchanges = new LinkedList<Exchange>(
            this.applicationContext.getBeansOfType(Exchange.class).values());
    // 2. 从 Spring 容器中获取所有的 Queue
    Collection<Queue> contextQueues = new LinkedList<Queue>(
            this.applicationContext.getBeansOfType(Queue.class).values());
    // 3. 从 Spring 容器中获取所有的 Binding
    Collection<Binding> contextBindings = new LinkedList<Binding>(
            this.applicationContext.getBeansOfType(Binding.class).values());
    // 4. 取出 Spring 容器中所有的 Collection 对象
    @SuppressWarnings("rawtypes")
    Collection<Collection> collections = this.declareCollections
            ? this.applicationContext.getBeansOfType(Collection.class, false, false).values()
            : Collections.<Collection>emptyList();
    
    // 5. 遍历 Collection， 把 exchange、queue 和 Binding 加到各自的 collection 中
    for (Collection<?> collection : collections) {
        if (collection.size() > 0 && collection.iterator().next() instanceof Declarable) {
            for (Object declarable : collection) {
                if (declarable instanceof Exchange) {
                    contextExchanges.add((Exchange) declarable);
                }
                else if (declarable instanceof Queue) {
                    contextQueues.add((Queue) declarable);
                }
                else if (declarable instanceof Binding) {
                    contextBindings.add((Binding) declarable);
                }
            }
        }
    }

    // 移除不需要使用 RabbitAdmin 声明的 exchange
    final Collection<Exchange> exchanges = filterDeclarables(contextExchanges);
    // 移除不需要使用 RabbitAdmin 声明的 queue
    final Collection<Queue> queues = filterDeclarables(contextQueues);
    // 移除不需要使用 RabbitAdmin 声明的 Binding
    final Collection<Binding> bindings = filterDeclarables(contextBindings);

    // 省略代码
    ···

    if (exchanges.size() == 0 && queues.size() == 0 && bindings.size() == 0) {
        this.logger.debug("Nothing to declare");
        return;
    }
    this.rabbitTemplate.execute(new ChannelCallback<Object>() {
        @Override
        public Object doInRabbit(Channel channel) throws Exception {
            // 正式声明 exchange
            declareExchanges(channel, exchanges.toArray(new Exchange[exchanges.size()]));
            // 正式声明 queue
            declareQueues(channel, queues.toArray(new Queue[queues.size()]));
            // 正式声明 binding
            declareBindings(channel, bindings.toArray(new Binding[bindings.size()]));
            return null;
        }
    });
    this.logger.debug("Declarations finished");
}
```

### RabbitMQ 声明式配置
    RabbitMQ 的声明式配置主要使用 @Bean 注解。
   测试代码见 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/blob/master/rabbitmq-spring/src/imooc/rabbitmq/spring/config/RabbitMQConfig)

### RabbitTemplate
    RabbitTemplate，即消息模板，是在与Spring AMQP 整合时进行消息发送的关键类。
    
    RabbitTemplate 提供了丰富的消息发送方法，包括可靠性投递消息方法、回调监听消息接口 `ConfirmCallback`、返回值确认接口 
    `ReturnCallback` 等等。同样，我们需要进行注入到 Spring 容器中，然后直接使用。
    
    在与Spring整合时需要实例化，但是在与 SpringBoot 整合时，在配置文件添加配置即可。
 测试代码见 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/blob/master/rabbitmq-spring/src/test/java/imooc/rabbitmq/spring/RabbitmqSpringApplicationTests.java)  testSendMessage() 和 testSendMessage2() 方法。
 
 
### SimpleMessageListenerContainer
    简单消息监听容器
    这个类非常强大，我们可以对它进行很多设置，对于消费者的配置项，这个类都可以满足；
#### 1. 功能
     1). 监听队列(多个队列)、自动启动、自动声明；   
     2). 设置事务特性、事务管理器、事务属性、事务容量(并发)、是否开启事务、回滚消息等；
     3). 设置消费者数量、最小最大数量、批量消费；
     4). 设置消息确认和自动确认模式、是否重回队列、异常捕获 handler 函数；
     5). 设置消费者标签生成策略、是否独占模式、消费者属性等；
     6). 设置具体的监听器、消息转换器等；
     
    注意：SimpleMessageListenerContainer 可以进行动态设置，比如在运行中的应用可以动态的修改其消费者数量的大小，接收消息的模式等。 
    很多基于 RabbitMQ 的自定制化后端管控台在进行动态设置的时候，也是根据这一特性去实现的。
    
### MessageListenerAdapter 
    消息监听适配器
    在该适配器中，有一个默认的监听消息的方法：handleMessage；当然这个方法的名字是可以自定义的，
    eg: `adapter.setDefaultListenerMethod("consumeMessage");`
    在该适配器中，还可以为队列指定一一对应的消息监听方法。这样，队列里面的消息都可以指定监听的方法。
    
```java
    Map<String, String> queueOrTagToMethodName = new HashMap<>();
    queueOrTagToMethodName.put("test.spring.bean.queue002", "handleQueue002");
    queueOrTagToMethodName.put("test.spring.bean.queue003", "handleQueue003");
    adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
```

### MessageConverter 消息转换器
    在进行发送消息的时候，正常情况下消息体为二进制数据的方法进行传输，如果希望内部帮我们进行转换，或者指定自定义的转换器，就需要用到 
    MessageConverter。
#### 1. 自定义消息转换器
    自定义消息转换器一般都需要实现 `MessageConverter` 接口，并且重写其中的 `toMessage` 和 `fromMessage` 方法。
#### 2. Jackson2JsonMessageConverter
    可以进行 Java 对象的转换功能;
    json 类型消息的监听方法的参数是 Map 类型的。
    注意：发送的消息的 contentType 必须是 application/json 格式的。
#### 3. DefaultJackson2JavaTypeMapper
    进行 Java 对象映射关系，能够把 json 类型的消息在监听时直接转换为 Java 对象。
    注意发送消息时必须在消息属性的 header 中添加一个参数,参数名为 `__TypeId__`， 值为 对象类的全限定名。
#### 4. 自自定义二进制转换器：比如图片类型，PDF，PPT，流媒体
   测试代码见 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/blob/master/rabbitmq-spring/src/test/java/imooc/rabbitmq/spring/RabbitmqSpringApplicationTests.java)  testSendExtMessageListenerConverter() 方法。      