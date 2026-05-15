### RabbitMQ 面试题

#### 1、消息队列的作用与使用场景

**异步** ：银行转账、短信发送、消息推送；

**解耦** ：购物下单的订单、库存、用户账户等之间的解耦；

**削峰** ：秒杀等并发量高的场景;

**广播通信** : 订单系统的退货需要通知很多第三方系统的场景；

总结:

​       对于数据量大或处理耗时长的操作，可以引入MQ实现异步通信，较少客户端的等待，提升响应速度，优化客户体验；

​      对于改动影响大的系统之间，可以引入MQ实现解耦，减少系统之间的直接依赖，提升可维护性和可扩展性；

​     对于会出现瞬间流量峰值的系统，可以引入MQ实现流量削峰，达到保护应用和数据库的目的；

​     一对多的广播通信；

#### 2、channel 的作用是什么？

​     channel 是 RabbitMQ 的重要概念，它是一个虚拟的连接。所有 AMQP 的**命令**都是通过 channel 发送的，切每一个 channel 都有唯一的ID。

* 一个 channel 只能被单独的一个操作系统线程使用，故投递到特定的 channel 上的 message 是有顺序的。但一个操作系统线程上可以使用多个 channel。
* 编号为 0 的 channel 用于处理所有对于当前 connection 全局有效的帧，而 1-65535 号用于处理和特定 channel  相关的帧。

#### 3、多个项目使用同一个 MQ服务器，怎么实现权限隔离

​    每一个 RabbitMQ 服务器都能创建虚拟消息服务器，我们称之为虚拟主机(vhost)。每一个 vhost 本质上是一个 mini 版的 RabbitMQ 服务器，拥有自己的队列、交换机、绑定和权限控制。vhost 对于 Rabbit 就像虚拟机对于服务器一样，他们通过在各个实例间提供逻辑上分离，允许为不同的应用程序安全保密的运行数据。所以，vhost既能将同一个 RabbitMQ 的众多客户区分开来，又可以避免队列和交换器的命令冲突。

#### 4、RabbitMQ 的消息有哪些路由方式、适用于什么场景

​	在 RabbitMQ 中，消息的传输路径是：消息生产方  --->   交换机(Exchange)  ---> 队列   ---> 消息消费方。消息在到达交换机之后，RabbitMQ 会将消息的 routingKey 与队列的 routingKey 进行匹配，继而把消息传输到对应的队列，最后被队列上绑定的消息消费方消费。

常用的交换机有一下三种：

**1)、Direct 直连**

​       直连类型的交换机与队列绑定需要指定一个明确的绑定键(binding key)。当消息的路由键与某个队列的绑定建完全匹配时，消息才会从交换机上路由到这个队列上。多个队列也可以使用相同的绑定键。

​	使用场景：适用于一些业务用途明确的消息。

**2)、Topic 主题**

 	Topic 类型的交换机与队列绑定时可以使用通配符类型的绑定建。支持两个通配符#(代表0个或多个单词)和*(代表不多不少一个单词)。

​	使用场景：适用于一些根据业务主题或者消息登记过滤消息的场景。比如说一条消息可能跟资金有关，又跟风控有关，那就可以让这个消息指定一个多级的路由键，第一个单词代表跟资金相关，第二个代表跟风控相关。这样，下游的业务系统的队列就可以使用不同的绑定建去接口消息了。

**3)、Fanout 广播**

​	广播类型的交换机与队列绑定时，不需要指定绑定建。因此生产者发送消息到广播类型的交换机上，也不需要携带路由键。消息到达交换机时，所有与交换机绑定的队列都会收到相同的消息的副本。

​	使用场景：适用于一些通用的业务消息。比如产品系统产品数据变化的消息，是所有的系统都会用到的，就可以创建一个广播类型的交换机，大家自己建队列就可以使用了。

#### 5、交换机与队列、队列与消费者的绑定关系是什么样的

​	交换机与队列之间通过 binding key 进行绑定，一个交换机可以绑定多个队列。

​	队列与消费者之间通过 channel 与队列进行，一个消费者只能绑定一个队列。

#### 6、无法路由的消息去了哪里

​	在消息传输到 Exchange 之后，有可能因为以下原因造成消息无法路由：

1. Exchange 没有任何 Queue 与之绑定
2. 根据消息的路由键，没有任何一个合适的 Queue 来投递消息     

   对于这些失败的消息，有两种处理方式：

1. 将消息返回给投递该消息的生产者

   ```java
    void basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body) throws IOException;
   ```

   生产方在发送消息可以设置一个 `mandatory` 参数为 true,。这样，交换机在消息无法路由时，就会把消息返回给生产方，生产方可以通过添加 `ReturnListener` 实现退回消息的处理。

   示例代码：

   ```java
   channel.addReturnListener(new ReturnListener() {
               public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                   System.out.println("=========监听器收到了无法路由，被退费的消息============");
                   System.out.println("replyText:" + replyText);
                   System.out.println("exchange:" + exchange);
                   System.out.println("routingKey:" + routingKey);
                   System.out.println("message:" + new String(body));
               }
           });
   channel.basicPublish("", "exchange", true, null, "退回消息".getBytes());
   ```

2. 使用备份交换机 alternate-exchange

   使用备份交换机只须要给交换机绑定一个备份交换机便可，当消息路由失败以后，消息将投递到备份交换机，再由备份交换机路由消息到备份队列。这样咱们只须要关注这个备份队列就能知道/获取到路由失败的消息。

   **注意**：在使用备份交换机后，第一种使用 `mandatory`参数的退回消息的方式将失效。

#### 7、消息在什么情况下会变成死信

​	消息变成死信的前提时消息过期，消息过期主要有一下两个原因：

	1. 消息设置了过期时间，过了这个时间仍然没有消费方进行消费；
 	2. 队列设置了过期时间，过了这个时间仍然没有消费方进行消费；
 	3. 消息超过队列长度或容量；
 	4. 消息被拒绝并且未设置重回队列；

 消息在投递到 Queue之后过期了，如果没有任何配置，这个消息就会被丢弃。我们也可以通过配置让这种过期的消息变成死信，在别的地方存储。通过给队列指定(创建队列是指定)一个死信交换机(DLX)，在死信交换机上绑定的队列被称之为死信队列(DLQ)，DLX实际上也是普通的交换机，DLQ也是普通的队列。在给队列配置 DLX 后，如果消息过期了，就会把这个小夏发送到 DLX，继而路由到DLX上绑定的DLQ上，消息到达 DLQ之后，就可以被消费方消费了。所以死信的流转流程就是：

​    生产者   --->  原交换机   --->  原队列(超过 TTL 之后)   ---> 死信交换机   --->  死信队列  ---->  最终消费者

#### 8、RabbitMQ 如何实现延迟消息

​	延迟消息的实现方式：

1. 通过队列或者消息的过期时间实现；

   缺点：

   - 如果使用队列来设置消息的 TTL，在消息过期时间梯度非常多的情况下，比如1分钟、2分钟....则需要创建很多的交换机和队列。
   - 如果单独设置消息的 TTL，则可能会造成队列的消息阻塞（前一条消息没有出队消费，后面的消息无法投递。比如第一条消息的 TTL 是 30min，第二条消息的 TTL 是 10分钟，那么10分钟后，即使第二条消息应该投递了，但由于第一条消息还未出队，所以无法投递 ）。
   - 可能存在一定的时间误差。

2. 通过 `rabbitmq-delayed-message-exchange` 插件来实现延迟队列的功能。（在 RabbbitMQ 3.5.7 及之后的版本才有）；

#### 9、有哪些情况会导致消息丢失，怎么解决

消息在RabbitMQ服务器传递的过程如下：![image-20210519133819156](C:\Users\sangaizhi\AppData\Roaming\Typora\typora-user-images\image-20210519133819156.png)

在上面的过程中的四个步骤都有可能发生消息的丢失

1. 消息发送到 RabbitMQ 服务器

   消息发送到 RabbbitMQ 服务器的 Broker 的过程中，可能因为网络连接或者 Broker 故障(磁盘写满等)导致消息发送失败，消息生产者不能确定 Broker 有没有正确接收消息。对此，RabbitMQ 提供了两种机制服务端确认机制，也就是RabbitMQ服务端在接收到消息给生产者一个应答。

   * 事务（Transaction)模式 

     ​    把信道 Channel 设置成事务模式(`channel.txSelect()`)，然后发布消息给 RabbitMQ,如果 `channel.txCommit()` 方法调用成功,则说明事务提交成功，则消息y一定到达了 RabbitMQ。如果事务提交执行之前由于 RabbitMQ 异常崩溃或者其他原因抛出异常，这个时候我们可以将其捕获，进而通过执行`channel.txRollback()` 方法实现事务回滚。在事务模式里面，只有收到了服务端的 Commit-OK 的指令，才能提交成功。所以可以解决生产者和服务端确认的问题。

     缺点：事务模式是阻塞的，一条消息没有发送完毕，不能发送另外一条消息，它会榨干 RabbitMQ 的性能，不建议生成使用。

     示例代码：

     ```java
       public void send() throws Exception {
             ConnectionFactory factory = new ConnectionFactory();
             factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
             // 建立连接
             Connection conn = factory.newConnection();
             // 创建消息通道
             Channel channel = conn.createChannel();
             String msg = "Hello world, Rabbit MQ";
             // 声明队列（默认交换机AMQP default，Direct）
             // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
             try {
                 channel.txSelect();
                 // 发送消息
                 // String exchange, String routingKey, BasicProperties props, byte[] body
                 channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
                 channel.txCommit();
                 int i =1/0;
                 channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
                 channel.txCommit();
                 System.out.println("消息发送成功");
             } catch (Exception e) {
                 channel.txRollback();
                 System.out.println("消息已经回滚");
             }
             channel.close();
             conn.close();
         }
     ```

     

   * 确认（Confirm）模式

     确认模式有三种方式

     * 普通确认模式

       在生产者调用 `channel.confirmSelect()`方法把 channel 设置成 confirm 模式后，然后发送消息。一旦消息被投递到交换机后（跟是否路由到队列无关）,RabbitMQ 就会发送一个确认(Basic_Ack)给生产者，也就是调用 `channel.waitForConfirms`返回 true,这样生产者就知道消息已被接收。如果网络错误，会抛出连接异常；如果交换机不存在，会抛出 404错误。

       缺点：发送一条确认一条，效率不高

       示例代码：

       ```java
       channel.confirmSelect();
       channel.basicPublish("1", QUEUE_NAME, null, msg.getBytes());
       // 普通Confirm，发送一条，确认一条
       if (channel.waitForConfirms()) {
           System.out.println("消息发送成功" );
       }else{
           System.out.println("消息发送失败");
       }
       ```

     * 批量确认模式

       批量确认是通过调用 `chahnel.waitForConfirmsOrDie()`方法的结果来确定服务器是否接收到消息。如果没有抛出异常，就代表消息都被服务端接收了。批量确认的结果，ACK如果是Multiple=True，代表ACK里面的Delivery-Tag之前的消息都被确认了，比如5条消息可能只收到1个ACK，也可能收到2个（抓包才看得到），直到所有信息都发布，只要有一个未被Broker确认就会IOException。

       缺点：单次确认发送的消=息数量难以确定，太低效率不行，太高就会产生另外一个问题，如果最后一条消息被拒绝，则前面的所有消息都需要重发。

       代码示例：

       ```java
       try {
           channel.confirmSelect();
           for (int i = 0; i < 5; i++) {
               channel.basicPublish("", QUEUE_NAME, null, (msg +"-"+ i).getBytes());
           }
           channel.waitForConfirmsOrDie();
           System.out.println("消息发送完毕，批量确认成功");
       } catch (Exception e) {
           // 发生异常，可能需要对所有消息进行重发
           e.printStackTrace();
       }
       ```

       

     * 异步确认模式

       一边发送一边确认，异步确认模式需要添加一个 ConfirmListener, 并且用一个 SortedSet 来维护批次中没有被确认的消息。

       代码示例：

       ```java
       // 用来维护未确认消息的deliveryTag
       final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
       channel.addConfirmListener(new ConfirmListener() {
           public void handleNack(long deliveryTag, boolean multiple) throws IOException {
               System.out.println("Broker未确认消息，标识：" + deliveryTag);
               if (multiple) {
                   // headSet表示后面参数之前的所有元素，全部删除
                   confirmSet.headSet(deliveryTag + 1L).clear();
               } else {
                   confirmSet.remove(deliveryTag);
               }
               // todo 应该进行消息的重发
           }
           public void handleAck(long deliveryTag, boolean multiple) throws IOException {
               // 如果true表示批量执行了deliveryTag这个值以前（小于deliveryTag的）的所有消息，如果为false的话表示单条确认
               System.out.println(String.format("Broker已确认消息，标识：%d，多个消息：%b", deliveryTag, multiple));
               if (multiple) {
                   // headSet表示后面参数之前的所有元素，全部删除
                   confirmSet.headSet(deliveryTag + 1L).clear();
               } else {
                   // 只移除一个元素
                   confirmSet.remove(deliveryTag);
               }
               System.out.println("未确认的消息:"+confirmSet);
           }
       });
       
       // 开启发送方确认模式
       channel.confirmSelect();
       for (int i = 0; i < 10; i++) {
           long nextSeqNo = channel.getNextPublishSeqNo();
           // 发送消息
           // String exchange, String routingKey, BasicProperties props, byte[] body
           channel.basicPublish("", QUEUE_NAME, null, (msg +"-"+ i).getBytes());
           confirmSet.add(nextSeqNo);
       }
       System.out.println("所有消息:"+confirmSet);
       // 这里注释掉的原因是如果先关闭了，可能收不到后面的ACK
       //channel.close();
       //conn.close();
       ```

2. 消息从交换机路由到队列

   在这个环节中有可能因为 routingkey 错误或者队列不存在的问题造成消息丢失。我们有两种方法处理无法路由的消息。

   * 让服务端重发给生成者

     该方式通过发送时设置 `mandatory`参数为 true和添加  ReturnListener 实现消息重新发送会生产者。

     代码示例:

     ```java
     channel.addReturnListener(new ReturnListener() {
         public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
             System.out.println("监听器收到了无法路由，被退回的消息");
         }
     });
     AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).
         contentEncoding("UTF-8").build();
     // 第三个参数是设置的mandatory，如果mandatory是false，消息也会被直接丢弃
     channel.basicPublish("", "exchange", true, properties, "丢失消息测试".getBytes());
     ```

     

   * 让交换机路由到另外一个备份的交换机

     在创建交换机的时候，从属性中指定备份交换机。

     ```java
     Map<String,Object> arguments = new HashMap<String,Object>();
     arguments.put("alternate-exchange","ALTERNATE_EXCHANGE");
     
     channel.exchangeDeclare("TEST_EXCHANGE","topic", false, false, false, arguments);
     ```

3. 消息在队列存储

   在这环节，如果没有消费者的话，队列一直存在在数据库中。此时如果 RabbitMQ 的服务发送故障，可能导致内存中的消息丢失，所以我们要把消息本身和元数据(队列、交换机、绑定)都保存到磁盘。解决方案：

   * 队列持久化

     ```java
     // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
     channel.queueDeclare("QUEUE_NAME", true, false, false, null);
     ```

     durable：是否持久化队列。没有持久化的队列保存在内存中，服务重启后队列和消息都会丢失。

     exclusive：是否排他。排他性队列的特点：只对首次声明它的连接可见、会在其连接断开的时候自动删除。

     autoDelete: 没有消费者连接的时候，自动删除

   * 交换机持久化

     ```java 
     new DirectExchange("DIRECT_EXCHANGE", true, false);
     ```

     

   * 消息持久化

     ```java
     AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                     .deliveryMode(2) // 表示持久化消息
                     .contentEncoding("UTF-8")
                     .expiration("10000") // TTL
                     .build();
     ```

   * 集群

4.  消息投递到消费者

   如果消费者收到消息后还没处理或者处理过程中发生异常，会导致这个环节失败。所以服务端应该以某种方式和得知消费者对消息的接收情况，并决定是否重新投递这条消息给其他消费者。RabbitMQ 提供了消费者的消息确认机制，消费者可以自动或者手动的发送 ACK 给服务端。针对没有收到消费者 ACK  的消息，在消费者断开连接后，RabbitMQ 会把这条消息发送给其他消费者。如果没有其他消费者，消费者在重启后会重新消费这条消息，重复执行业务逻辑。消费者给 Broker 的应答有两种方式

   * 自动 ACK

     自动 ACK 也是默认的消费者应答方式，在消费者接收到这条消息后就会自动应答服务端，并不会关注消费者业务代码的执行情况。

   * 手动 ACK 

     如果需要等待消费者执行完业务代码才发送 ACK，需要把自动 ACK 设置成手动 ACK（`channel.basicConsume("QUEUE_NAME", true, consumer);`）。这个时候 RabbitMQ 会等待消费者显式的回复 ACK 后才从队列中移除消息。

     如果消费出了问题，不能发送ACK给服务端，我们可以通过拒绝消费消息的方法让消息重新入队。有两种拒绝方式：

     * basicReject() 拒绝单条

       ```java
       //requeue：是否重新入队列，true：是；false：直接丢弃，相当于告诉队列可以直接删除掉
       //TODO 如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
       channel.basicReject(envelope.getDeliveryTag(), false);
       ```

     * basicNack() 批量拒绝

       ```java
       // requeue：是否重新入队列
       // TODO 如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
       channel.basicNack(envelope.getDeliveryTag(), true, true);
       ```

总结：从生成者发送消息的 Broker，交换机到队列，队列本身，队列到消费者，这中间都有可能造成消息丢失，我们也都有对应的方法知道消息有没有正常流转并且没有流转时采取相关措施。

#### 10、一个队列最多可以存放多少条消息

默认情况下是不限制队列的大小，可以通过 `x-max-length`参数控制队列的最大消息数量，通过`x-max-length-bytes` 控制队列里面所有消息的总和字节数。

#### 11、可以通过队列的 x-max-length 最大消息数来实现限流吗？

不能，因为队列在达到最大数量时，会删除先入队的消息来保证最终数量。

#### 12、如何提高消息的消费速率

在不能提高消费者业务代码的情况下，只能通过增加消费者数量的方式提高消费速率。

#### 13、AmqpTemplate 和 RabbitTemplate 的区别

Spring AMQP 是 Spring整合 AMQP协议 的一个抽象，Rabbit 是一个实现。

#### 14、如何动态的创建队列和消费者

 	可以通过 MessageListenerContainer 实现

```java
@Bean
public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    container.setQueues(getSecondQueue(), getThirdQueue()); //监听的队列
    container.setConcurrentConsumers(1); // 最小消费者数
    container.setMaxConcurrentConsumers(5); //  最大的消费者数量
    container.setDefaultRequeueRejected(false); //是否重回队列
    container.setAcknowledgeMode(AcknowledgeMode.AUTO); //签收模式
    container.setExposeListenerChannel(true);

    container.setConsumerTagStrategy(new ConsumerTagStrategy() {    //消费端的标签策略
        @Override
        public String createConsumerTag(String queue) {
            return queue + "_" + UUID.randomUUID().toString();
        }
    });
    return container;
}

```

#### 15、Spring AMQP  消息怎么封装，用什么转换

在 Spring AMQP中使用 `org.springframework.amqp.core.Message` 封装消息，构造方法是 `public Message(byte[] body, MessageProperties messageProperties)`。

使用 `org.springframework.amqp.support.converter.MessageConverter`转换消息。

```java
public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(new Jackson2JsonMessageConverter()); // 消息转换器
    factory.setAcknowledgeMode(AcknowledgeMode.NONE); // 签收模式
    factory.setAutoStartup(true);
    factory.setConcurrentConsumers(2); // 最小消费者数
    factory.setMaxConcurrentConsumers(6); //最大消费者数
    return factory;
}
```

#### 16、如何保证消息的顺序性

一个队列只有一个消费者。

#### 17、RabbitMQ 如何实现高可用？

RabbitMQ 的高可用，是基于**主从**做高可用性的。它有三种模式：

- 单机模式
- 普通集群模式
- 镜像集群模式

**1）单机模式**

单机模式，就是启动单个 RabbitMQ 节点，一般用于本地开发或者测试环境。实际生产环境下，基本不会使用。

 **2) 普通集群模式（无高可用性）**

普通集群模式，意思就是在多台机器上启动多个 RabbitMQ 实例，每个机器启动一个。

- 你**创建的 queue，只会放在一个 RabbitMQ 实例上**，但是每个实例都同步 queue 的元数据（元数据可以认为是 queue 的一些配置信息，通过元数据，可以找到 queue 所在实例）。
- 你消费的时候，实际上如果连接到了另外一个实例，那么那个实例会从 queue 所在实例上拉取数据过来。

[![架构图](http://static.iocoder.cn/75d36ed17c91932e28b5eeba681ad8ec)](http://static.iocoder.cn/75d36ed17c91932e28b5eeba681ad8ec)架构图

这种方式确实很麻烦，也不怎么好，**没做到所谓的分布式**，就是个普通集群。因为这导致你要么消费者每次随机连接一个实例然后拉取数据，要么固定连接那个 queue 所在实例消费数据，前者有**数据拉取的开销**，后者导致**单实例性能瓶颈**。

而且如果那个放 queue 的实例宕机了，会导致接下来其他实例就无法从那个实例拉取，如果你**开启了消息持久化**，让 RabbitMQ 落地存储消息的话，**消息不一定会丢**，得等这个实例恢复了，然后才可以继续从这个 queue 拉取数据。

所以这个事儿就比较尴尬了，这就**没有什么所谓的高可用性**，**这方案主要是提高吞吐量的**，就是说让集群中多个节点来服务某个 queue 的读写操作。

 **3) 镜像集群模式（高可用性）**

这种模式，才是所谓的 RabbitMQ 的高可用模式。跟普通集群模式不一样的是，在镜像集群模式下，你创建的 queue，无论元数据还是 queue 里的消息都会**存在于多个实例上**，就是说，每个 RabbitMQ 节点都有这个 queue 的一个**完整镜像**，包含 queue 的全部数据的意思。然后每次你写消息到 queue 的时候，都会自动把**消息同步**到多个实例的 queue 上。

[![架构图](http://static.iocoder.cn/20a6c4d82a08becf7e8d913662b00357)](http://static.iocoder.cn/20a6c4d82a08becf7e8d913662b00357)架构图

那么**如何开启这个镜像集群模式**呢？其实很简单，RabbitMQ 有很好的管理控制台，就是在后台新增一个策略，这个策略是**镜像集群模式的策略**，指定的时候是可以要求数据同步到所有节点的，也可以要求同步到指定数量的节点，再次创建 queue 的时候，应用这个策略，就会自动将数据同步到其他的节点上去了。

这样的话，好处在于，你任何一个机器宕机了，没事儿，其它机器（节点）还包含了这个 queue 的完整数据，别的 consumer 都可以到其它节点上去消费数据。坏处在于，第一，这个性能开销也太大了吧，消息需要同步到所有机器上，导致网络带宽压力和消耗很重！第二，这么玩儿，不是分布式的，就**没有扩展性可言**了，如果某个 queue 负载很重，你加机器，新增的机器也包含了这个 queue 的所有数据，并**没有办法线性扩展**你的 queue。你想，如果这个 queue 的数据量很大，大到这个机器上的容量无法容纳了，此时该怎么办呢？

#### 18、Rabbit MQ  的集群节点类型

磁盘节点和内存节点



#### 19、如果一个项目需要从多个服务器接收消息，怎么做；如果一个项目需要发送消息到多个服务器，怎么做

定义多个 ConnectionFactory，注入到消费者监听类/Template。