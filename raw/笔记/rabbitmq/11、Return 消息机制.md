### 一、什么是 Return 消息机制
    Return 消息机制是指在生产端添加 Return Listener 用于处理一些不可路由的消息。
    在某些情况下，我们在发送消息的时候，当前的 exchange 不存在或者指定的 routingKey 路由不到，这个时候如果我们需要监听这些不可
    达的消息，就需要使用 Return Listener；
### 二、基础 API 的关键配置项
    1、Mandatory
       如果为 true, 则监听器会接受到路由不可达的消息，然后进行后续处理，如果为 false, 那么 broker 端会自动删除该消息；
   测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/returnlistener)
           
    