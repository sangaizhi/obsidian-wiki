/**
 * @author: saz
 * @date 2019/4/18 22:06
 */
package imooc.rabbitmq.springboot.consumer.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;

public class RabbitReceiver {

    /**
     * 消息监听
     *
     * @param message
     * @param channel
     * @throws Exception
     */
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
}
