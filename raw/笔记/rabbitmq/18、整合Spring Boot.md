#### 1. 创建两个 Springboot 项目
    第一步，我们需要创建两个 Springboot 项目：
       1). rabbitmq-spring-boot-producer
       2). rabbitmq-spring-boot-consumer
#### 2. 配置 Springboot 项目
    1). 两个项目的都需要做的配置
```yaml
spring:
  rabbitmq:
    addresses: 129.211.100.215:5672
    username: guest
    password: guest
    connection-timeout: 15000
    virtual-host: /
```       
    2). rabbitmq-spring-boot-producer 项目还需要一些额外的核心配置
```yaml
spring:
  rabbitmq:
    ## 消息推送确认模式开启配置
    publisher-confirms: true
    ## 消息发送失败返回开启配置，如果设置为 true,则 spring.rabbitmq.template.mandatory 也要设置为 true
    publisher-returns: true
    ## 当消息发送失败时，把消息返回到生产端
    template:
      mandatory: true
    ## 其他配置，eg: 发送重试，超时时间，次数，间隔等  
```       
    3). rabbitmq-spring-boot-consumer 项目还需要一些额外的核心配置
```yaml
spring:
  rabbitmq:
    listener:
      simple:
        ## 消息签收模式：手动签收，保证消息的可靠性送达，或者消息端消费失败的时候可以做重回队列，根据业务记录日志等处理
        acknowledge-mode: manual
        ## 最小监听消息的线程数
        concurrency: 5
        ## 最大监听消息的线程数
        max-concurrency: 10

```
#### 3. 生产服务配置 publisher-confirm 和 publisher-returns
      publisher-confirms: 实现一个监听器用于监听 Broker 端给我们返回的确认请求，关键接口是 RabbitTemplate.ConfirmCallback
      实现方式：
```java
public final class CustomConfirmCallback implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("correlationData:"+correlationData);
        System.out.println("ack:"+ack);
        if(!ack){
            System.out.println("异常处理");
        }
    }
}
```
      publisher-returns: 保证消息对 Broker 端是可达的，如果出现 RoutingKey 不可达的情况，则使用监听器对不可达的消息进行后续
           处理，保证消息的路由成功。关键接口是：RabbitTemplate.ReturnCallback。
       注意：在发送消息的时候须对 template 进行配置 mandatory=true来保证 return 监听有效。 
```java
public class CustomReturnCallback implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("return exchange" + exchange + ", routingKey:" + routingKey + ", replyCode:" + replyCode + ", replyText:" + replyText);
    }
}
```    
#### 4. RabbitMQListener 注解
     这是一个组合注解，里面可以注解配置 @QueueBinding、@Queue、@Exchange 直接通过这个组合注解一次性搞定消费端交换机、队列、绑定
     路由、并且配置监听功能。
     使用示例：
```java
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "test.spring.boot.queue-1", declare = "true"),
        exchange = @Exchange(value = "test.spring.boot.exchange-1",
                durable = "true",
                type = "topic",
                ignoreDeclarationExceptions = "true"),
        key = "test.spring.boot.routingkey-1"
))
@RabbitHandler
public void onMessage(Message message, Channel channel) throws Exception {
    System.out.println("消息体内容：" + message.getPayload());
    Long deliverTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
    System.out.println("deliverTag:" + deliverTag);
    // 手动 ack
    channel.basicAck(deliverTag, false);
}
```