### 一、 TTL
    TTL 是 Time TO Live 的缩写，也就是生存时间
    RabbitMQ 支持消息的过期时间，在消息发送时可以进行指定；
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/ttl/message)
    
    RabbitMQ 支持队列的过期时间，从消息入队列开始计算，只要超过了队列的超时时间配置，那么消息就会自动的清除。
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/ttl/queue)