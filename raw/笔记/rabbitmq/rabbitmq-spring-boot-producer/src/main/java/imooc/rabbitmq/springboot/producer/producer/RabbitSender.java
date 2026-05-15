/**
 * @author: saz
 * @date 2019/4/18 21:09
 */
package imooc.rabbitmq.springboot.producer.producer;


import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;
import java.util.UUID;

public class RabbitSender {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    public void send(Object messageBody, Map<String, Object> properties) throws Exception{
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message message = MessageBuilder.createMessage(messageBody, messageHeaders);
        rabbitTemplate.setConfirmCallback(new CustomConfirmCallback());
        rabbitTemplate.setReturnCallback(new CustomReturnCallback());
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString()); //这个Id 应该是全局唯一，在后续做消息尝试的时候有用到
        rabbitTemplate.convertAndSend("test.spring.boot.exchange-1", "test.spring.boot.routingkey-1", message, correlationData);

    }
}
