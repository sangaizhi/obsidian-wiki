### AMQP: 
#### 介绍：
    高级消息队列协议(Advanced  Message Queuing Protocol) 
    二进制协议，是一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。
#### 协议模型：
![AMQP协议模型](https://images.gitee.com/uploads/images/2019/0321/154420_517116e1_693323.png "AMQP协议模型.png")

    1. Publisher Application: 消息的生产者
    2. Consume Application：消息的消费者
    3. Server：RabbitMQ 节点
    4. Vistual host: 虚拟主机，是一个逻辑概念，主要作用就是一个上层的路由
    5. Exchange: 交换机（AMQP协议的核心），生产者吧产生的消息投递到Exchange.
    6. Consume Application: 消费者，主需要监听Message Queue，Message Queue 中一有消息就进行消费
    7. Message Queue：与 Exchange 进行绑定。

消息在AMQP协议中流转的过程：

    1. Publisher 产生消息，并且将消息投递到 Exchange 中。投递的过程主要是通过IP和端口找到 Server,再在 Server中通过一定的路由规则找到对应的 Virtual host,最后在找到对应的Exchange,
    2. 当消息投递到 Exchange 中后，就会通过 routing key 把消息传递到对应绑定的 Message Queue 中。
    3. 当Consumer 监听到 Message Queue有消息时，就会去消费这个消息。
#### 核心概念：
    1. Server： 又称 Broker，接受客户端的连接，实现AMQP实体服务
    2. Connection: 连接，应用程序与 Broker 的网络连接
    3. Channel: 网络信道，几乎所有的操作都在 Channel 中进行， Channel是进行消息读写的通道。客户端可建立多个 Channel，每个 Channel 代表一个会话任务。
    4. message: 消息，服务器与应用程序之间传送的数据，由 Properties 和 Body组成。Properties可以对消息进行修饰，比如消息的优先级、延迟等高级特性；Body 则是消息实体内容。
    5. Virtual Host: 虚拟主机，用于进行逻辑隔离，最上层的消息路由，一个 Virtual host 里面可以有若干个 Exchange 和 Queue，同一个 Virtual host 里面不能有相同名称的Exchange 或 Queue。
    6. Exchange： 交换机，接收消息，根据路由键转发消息到绑定的队列。
    7. Binding: Exchange 和 Queue之间的虚拟连接，binding 中可以包含 routing key
    8. Routing key: 一个路由规则，虚拟机可用它来确定如何路由一个特定消息
    9. Queue: 也称为 Message Queue，消息队列，保存消息并将它们转发给消费者
