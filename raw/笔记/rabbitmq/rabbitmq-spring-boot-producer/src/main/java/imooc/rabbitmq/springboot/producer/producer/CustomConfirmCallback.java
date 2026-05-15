/**
 * @author: saz
 * @date 2019/4/18 21:31
 */
package imooc.rabbitmq.springboot.producer.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public final class CustomConfirmCallback implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("correlationData:" + correlationData);
        System.out.println("ack:" + ack);
        if (!ack) {
            System.out.println("异常处理");
        }
    }
}
