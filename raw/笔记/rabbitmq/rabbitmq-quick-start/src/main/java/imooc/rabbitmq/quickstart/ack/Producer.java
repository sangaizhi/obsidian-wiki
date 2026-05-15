/**
 * @author: saz
 * @date 2019/4/6 23:14
 */
package imooc.rabbitmq.quickstart.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * ACK的生产端
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置连接工厂属性
        connectionFactory.setHost("129.211.100.215");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 创建连接
        Connection connection = connectionFactory.newConnection();

        // 创建 channel
        Channel channel = connection.createChannel();

        // 创建 exchange
        String exchange = "test.ack.exchange";
        channel.exchangeDeclare(exchange, "topic", true, false, null);
        // 声明 Routingkey
        String routingKey = "test.ack";

        // 发送消息

        for (int i = 0; i < 5; i++) {

            Map<String, Object> headers = new HashMap<>();
            headers.put("num", i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2).contentEncoding("UTF-8").headers(headers).build();
            String msg = "Hello RabbitMQ " + i + "th ACK Message!";
            channel.basicPublish(exchange, routingKey, false, properties, msg.getBytes());
        }
    }
}
