### 一、什么是 Confirm 消息确认机制
    消息的确认，是指生产者投递消息后，如果 Broker 收到消息，则会给生产者一个应答；
    生产者进行接受应答，用来确认这条消息是否正常的发送到 Broker，这种方式也是消息的可靠请投递的核心保障；
### 二、如何实现
    1、在 channel 上开启确认模式：channel.confirmSelect();
    2、在 channel 上添加监听：addConfirmListener，监听成功和失败的返回结果，根据具体的结果对消息进行重新发送或记录日期等
    后续处理；
  测试代码参考 [这里](https://gitee.com/sangaizhi/imooc-rabbitmq/tree/master/rabbitmq-quick-start/src/main/java/imoocrabbitmq/rabbitmqquickstart/confirm)
    