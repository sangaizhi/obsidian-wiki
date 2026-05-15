/**
 * @author: saz
 * @date 2019/4/6 23:24
 */
package imooc.rabbitmq.quickstart.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * dlx的消费端
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, KeyManagementException, NoSuchAlgorithmException {

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
        String exchangeName = "test.normal.exchange";
        String queueName = "test.normal.queue";
        String routingKey = "test.normal.key";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);


        // 死信队列的声明
        String dlxExchangeName = "test.dlx.exchange";
        String dlxQueueName = "test.dlx.queue";
        String dlxRoutingKey = "#";
        channel.exchangeDeclare(dlxExchangeName, "topic", true, false, null);
        channel.queueDeclare(dlxQueueName, true, false, false, null);
        channel.queueBind(dlxQueueName, dlxExchangeName, dlxRoutingKey);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", dlxExchangeName);
        channel.queueDeclare(queueName, true, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);


        channel.basicConsume(queueName, true, new CustomConsumer(channel));


    }
}
