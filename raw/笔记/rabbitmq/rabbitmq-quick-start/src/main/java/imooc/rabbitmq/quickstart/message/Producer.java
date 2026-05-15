/**
 * @author: saz
 * @date 2019/3/30 22:22
 */
package imooc.rabbitmq.quickstart.message;

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

        Map<String, Object> headers = new HashMap<>();
        headers.put("my-pro-1", "111");
        headers.put("my-pro-2", "222");

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000") // 过期时间，单位毫秒
                .headers(headers)
                .build();

        // 通过 channel 发送数据
        for (int i = 0; i < 5; i++) {
            // 第一个参数
            String msg = "Hello, RabbitMQ!";
            channel.basicPublish("", "test001", properties, msg.getBytes());
        }

        // 关闭连接
        channel.close();
        connection.close();
    }
}
