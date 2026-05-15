/**
 * @author: saz
 * @date 2019/4/6 23:24
 */
package imooc.rabbitmq.quickstart.custom.consume;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 自定义消费者的消费端
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
        String exchangeName = "test.custom.consumer.exchange";
        String queueName = "test.custom.consumer.queue";
        String routingKey = "test.custom.consumer";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, true, new CustomConsumer(channel));


    }
}
