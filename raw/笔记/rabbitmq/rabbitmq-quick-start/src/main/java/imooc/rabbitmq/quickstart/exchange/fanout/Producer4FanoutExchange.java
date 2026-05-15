/**
 * @author: saz
 * @date 2019/4/2 22:06
 */
package imooc.rabbitmq.quickstart.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer4FanoutExchange {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();

        // 设置连接属性
        connectionFactory.setHost("129.211.100.215");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        // 通过 connection 创建一个 Channel
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_fanout_exchange";

        // 发送
        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ 4 Fanout Exchange Message ...";
//            channel.basicPublish(exchangeName, "", null, msg.getBytes());
            channel.basicPublish(exchangeName, "sadasd", null, msg.getBytes());
        }

        // 关闭连接
        channel.close();
        connection.close();
    }
}
