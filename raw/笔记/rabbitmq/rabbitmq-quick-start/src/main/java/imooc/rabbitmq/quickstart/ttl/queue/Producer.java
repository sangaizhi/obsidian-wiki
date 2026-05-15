/**
 * @author: saz
 * @date 2019/4/7 22:20
 */
package imooc.rabbitmq.quickstart.ttl.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

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

        // 创建 exchange
        String exchangeName = "test.queue.ttl.exchange";
        String routingKey = "test.queue.ttl";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        String message = "Hello RabbitMQ  Send Queue TTL Message!";

        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

    }
}
