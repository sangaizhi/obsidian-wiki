/**
 * @author: saz
 * @date 2019/4/6 23:14
 */
package imooc.rabbitmq.quickstart.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * dlx的生产端
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

        // 创建 正常的 exchange
        String exchange = "test.normal.exchange";
        channel.exchangeDeclare(exchange, "topic", true, false, null);
        // 声明 Routingkey
        String routingKey = "test.normal.key";


        for (int i = 0; i < 1; i++) {
            // 发送消息
            String msg = "Hello RabbitMQ DLX Message!";
            // 设置消息的过期时间
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .contentEncoding("UTF-8").expiration("10000").build();
            channel.basicPublish(exchange, routingKey, false, properties, msg.getBytes());
        }
    }
}
