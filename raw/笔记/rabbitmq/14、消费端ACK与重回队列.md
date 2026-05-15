### 一、消费端手工 ACK 和 NACK
    消费端通过`String basicConsume() throws IOException`在消费消息的时候可以通过 autoAck 参数设置是否自动 ack 应答；
    如果设置为 true，消费端一旦正确消费消息，就会发送一个 ack 应答给 MQ Broker,这是 MQ Broker就会认为消费者已正确消费这条消息。
    如果设置为 false, 就需要消费端在正确消费完当前消息后，可以手动通过`channel.basicAck(deliveryTag, false)`发送一条 ack 应答
    给 MQ Broker；当然，如果我们的消费端没有正确消费这条消息，我们也可以通过 `void basicNack(long deliveryTag,
     boolean multiple, boolean requeue)` 发送一个 nack 给 MQ Broker, 一旦 MQ Broker 收到 消费端的 nack,就会把当前的消息再
     发送一次到消费端。
### 二、使用场景
     1. 消费端在进行消费的时候，可能会因为业务的异常导致某一条消息多次消费后还是不能正确消费，也就是多次消费的应答都是 nack，这个
     时候我们就需要在某次消费后给MQ Broker 发送 ack 的确认消息，但是，我们一定要记录这条没有正常消费的消息，以便后续进行补偿。
     
     2. 如果由于服务器宕机等严重问题，我们的 MQ Broker 就会收不到 ack 和 nack 应答,这个时候我们就需要手工进行 ack 保障消费端消费
      成功。
### 三、消费端的重回队列
    消费端重回队列是为了对没有处理成功的消息，把消息重新回递给 Broker;一般我们的实际应用中，都会关闭重回队列，也就是设置为 False.  
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/ack)    