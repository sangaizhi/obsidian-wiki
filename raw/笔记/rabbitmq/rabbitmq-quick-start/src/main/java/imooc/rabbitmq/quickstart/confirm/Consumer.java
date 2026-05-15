/**
 * @author: saz
 * @date 2019/3/30 22:22
 */
package imooc.rabbitmq.quickstart.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Confirm 确认消息的消费者
 */
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 创建 connection
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("129.211.100.215");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        // 通过 connection 创建一个 channel
        Channel channel = connection.createChannel();

        String exchange = "test-confirm-exchange";
        String routingKey = "confirm.*";
        String queueName = "test-confirm-queue";

        // 声明交换机、队列并绑定
        channel.exchangeDeclare(exchange, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchange, routingKey);


        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端：" + msg);
        }
    }
}
