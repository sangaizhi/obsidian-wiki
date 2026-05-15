/**
 * @author: saz
 * @date 2019/4/18 21:37
 */
package imooc.rabbitmq.springboot.producer.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class CustomReturnCallback implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("return exchange" + exchange + ", routingKey:" + routingKey + ", replyCode:" + replyCode + ", replyText:" + replyText);
    }
}
