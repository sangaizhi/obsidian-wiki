### 绑定(Binding) 
   1. 绑定是指 Exchange 和 Queue, Exchange 和 Exchange 之间的连接关系，当然两个 Exchange 绑定时，本身没有问题，  
    但是消息的路由节点比较长。
   2. Binding 中可以包含 RoutingKey 或者参数。 
### 消息队列(Queue)
   1. 功能：
      存储消息数据
   2：属性:
      Durability: 是否持久化(Durable：是，Transient:否)
      Auto delete: 入选yes,代表当最后一个监听被移除后，该 Queue 会自动删除
### 消息(Message) 
   1. 定义： 
      服务器与应用程序之间传送的数据，本质上就是一段数据。
   2. 组成：
      有 Properties 和 Payload(Body) 组成；
   3. 属性：
      delivery mode：送达模式，可以选择消息到达 broker 上，是持久化还是内存级别的非持久化；
      headers:支持自定义属性 ；
      content_type: 消息的类型；
      content_encoding: 消息的字符集；
      priority: 消息的优先级（0-9,数字越大，级别越高）；  
      correlation_id: 常用于消息的唯一性标识，一般会用业务码+时间戳作为唯一标识，这个属性可以用于消息的ack、路由、幂等等。
      reply_to: 用于消息失败时，把消息返回到哪个队列。
      expiration: 消息的过期时间，过期时间内没有被消费的话，就会被删除
      message_id: 消息的Id
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/message)
   
### 虚拟主机(Virtual host)   
   虚拟主机是一个虚拟地址，用于进行逻辑隔离，是最上层的消息路由
   一个 Virtual Host 里面可以有若干个 Exchange 和 Queue；当然，同一个 Virtual Host 里面不能有相同名称的 Exchange 获 Queue。