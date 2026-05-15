/**
 * @author: saz
 * @date 2019/4/7 22:25
 */
package imooc.rabbitmq.quickstart.ttl.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import imooc.rabbitmq.quickstart.ttl.CustomConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("www.sangaizhi.cn");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 获取连接
        Connection connection = connectionFactory.newConnection();

        // 创建 channel
        Channel channel = connection.createChannel();

        String exchangeName = "test.message.ttl.exchange";
        String routingKey = "test.message.ttl";
        String queueName = "test.message.ttl";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, false, new CustomConsumer(channel));

    }
}
