### Exchange 的作用
    Exchange 是用来接收消息，并且根据 routingkey 转发消息到所绑定的队列。
### Exchange 说明
    下图是关于 RabbitMQ 的Exchange 的说明
![RabbitMQ Exchange](https://images.gitee.com/uploads/images/2019/0330/234926_5c3be95e_693323.png)
    其中蓝色部分是生产端把消息投递到Exchange中，然后通过 RoutingKey把消息路由到具体的队列。
    其中绿色部分是消费端，这个部分就是消费端跟队里建立监听关系，然后从队列中消费消息。
    其中黄色部分就是Exchange 通过 RoutingKey把消息路由具体的队列。

### 交换机的属性
#### 1. Name
    交换机的名称;
#### 2. Type：
    交换机的类型;
#### 3. Durability
    是否需要持久化，true 表示持久化;    
#### 4. Auto Delete
    当最后一个绑定到 Exchange 上的队列删除后，自动删除该 Exchange;
#### 5. Internal
    当前 Exchange 是否用于 RabbitMQ 内部使用，默认为 false;
#### 6. Arguments
    扩展参数，用于扩展 AMQP 协议自制定化使用;   
     
#### 交换机的类型 
#### 1. Direct
   所有发送到 Direct Exchange 的消息被转发到 RoutingKey 中指定的Queue；
   注意：Direct 模式可以使用 RabbitMQ 自带的 Exchange:default Exchange,所以不需要将 Exchange 进行任何绑定操作;
   消息传递时，RoutingKey必须完全匹配才会被队列接收，否则该消息会被抛弃。
   
   ![Direct Exchange Message Routing](https://images.gitee.com/uploads/images/2019/0402/224136_6f4c1f6f_693323.png)
   
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/exchange/direct)
   
    
#### 2. Topic
   所有发送到 Topic Exchange 的消息被转发到所有关心 RoutingKey 中指定 Topic 的 Queue 上.
   Exchange 将 RoutingKey 和 Topic 进行模糊匹配，此时队列需要绑定一个 Topic.
   注意：可以使用通配符进行模糊匹配
   符号 "#" 匹配一个多个词
   符号 "*" 匹配不多不少一个词
   eg: "log.#" 能够匹配到 "log.info.oa"
       "log.*" 只会匹配到 "log.erro
       
   通过下图可以更好的理解 topic exchange 的消息路由过程
   
   ![Topic Exchange Message Routing](https://images.gitee.com/uploads/images/2019/0402/221226_58522c56_693323.png)    

   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/exchange/topic)
   
   注意：同一个队列在将 routing key 从 '#' 通配符换成 '*' 通配符之后，如果没有解绑之前的 '#' 通配符，那么这个队列就会绑定两个 routing key
   
#### 3. Fanout
   不处理 Routing key,只需要简单的将队列绑定到交换机上，发送到交换机的消息都会被转发到与该交换机绑定的所有队列上。
   Fanout 交换机转发的消息是最快的.
   消息路由示例图：
   
   ![Fanout_Exchange Message Routing](https://images.gitee.com/uploads/images/2019/0402/223218_7f677829_693323.png)      
   
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/exchange/fanout)          
           
#### 4. headers
#### 5. 通过插件安装的类型    