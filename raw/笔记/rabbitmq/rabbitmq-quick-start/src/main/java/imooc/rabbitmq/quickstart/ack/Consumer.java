/**
 * @author: saz
 * @date 2019/4/6 23:24
 */
package imooc.rabbitmq.quickstart.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ACk的消费端
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("www.sangaizhi.cn");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 创建连接
        Connection connection = connectionFactory.newConnection();

        // 声明 channel
        Channel channel = connection.createChannel();
        // 声明交换机、队列并绑定
        String exchangeName = "test.ack.exchange";
        String queueName = "test.ack.queue";
        String routingKey = "test.ack";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 手动签收，一定要关闭 autoAck
        channel.basicConsume(queueName, false, new CustomConsumer(channel));
    }
}
