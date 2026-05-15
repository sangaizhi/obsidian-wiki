/**
 * @author: saz
 * @date 2019/4/7 22:25
 */
package imooc.rabbitmq.quickstart.ttl.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import imooc.rabbitmq.quickstart.ttl.CustomConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        String exchangeName = "test.queue.ttl.exchange";
        String routingKey = "test.queue.ttl";
        String queueName = "test.queue.ttl";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        Map<String, Object> queueArgs = new HashMap<>();
        // 设置队列消息过期时间
        queueArgs.put("x-message-ttl", 10000);
        channel.queueDeclare(queueName, true, false, false, queueArgs);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, false, new CustomConsumer(channel));

    }
}
