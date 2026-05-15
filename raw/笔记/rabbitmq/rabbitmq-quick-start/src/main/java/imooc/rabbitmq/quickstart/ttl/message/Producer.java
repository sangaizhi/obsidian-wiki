/**
 * @author: saz
 * @date 2019/4/7 22:20
 */
package imooc.rabbitmq.quickstart.ttl.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
        String exchangeName = "test.message.ttl.exchange";
        String routingKey = "test.message.ttl";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        String message = "Hello RabbitMQ  Send Message TTL Message!";

        // 设置消息的过期时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .contentEncoding("UTF-8").expiration("10000").build();

        channel.basicPublish(exchangeName, routingKey, properties, message.getBytes());

    }
}
