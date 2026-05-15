/**
 * @author: saz
 * @date 2019/4/6 23:24
 */
package imooc.rabbitmq.quickstart.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费端限流的消费端
 */
public class  Consumer {
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
        String exchangeName = "test.qos.exchange";
        String queueName = "test.qos.queue";
        String routingKey = "test.qos";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 限流第一步，设置单次消费的消息条数
        channel.basicQos(0, 1, false);
        // 限流第二步，autoAck 必须设置为 false
        // 限流第三步，自定义消费端的ack
        channel.basicConsume(queueName, false, new CustomConsumer(channel));


    }
}
