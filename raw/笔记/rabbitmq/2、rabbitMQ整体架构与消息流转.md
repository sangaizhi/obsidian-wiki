### 整体架构
RabbitMQ的整体架构如下图：
![RabbitMQ整体架构](https://images.gitee.com/uploads/images/2019/0321/160134_89d0cbf3_693323.png)
   
    上图中的左右两边都是 clients，也就是我们的应用程序。左边代表消息的生产者，右边代表消息的消费者。
    中间的一部分是真正的 RabbitMQ，生产者生产消息并把消息投递到 Exchange，Exchange 会对消息进行路由并过滤传递到具体的一个 Message Queue
    中，然后消费者就从Message Queue 中取出消息进行消费。

### 消息流转
RabbitMQ的消息流转如下图：
![RabbitMQ消息流转](https://images.gitee.com/uploads/images/2019/0321/161405_8cebf224_693323.png)   
消息流转的主要过程就是：

    1. 生产者产生 message，并且把 message 根据IP和端口投递到 Server 的 Exchange 中；
    这一步涉及到 Server中Virtual host的路由规则。
    2. Exchange 根据 message 中的 routing key 把消息传递到绑定的 Message Queue中； 
    在这里，一个 Exchange 可以绑定多个 Message Queue，但时针对一个消息，只会把这个消息传递到根据routing key确定的消息队列中。
    所以在图中，只有一个 Message Queue 中有 message.
    3. 消费者程序从监听的队列中取出消息。  