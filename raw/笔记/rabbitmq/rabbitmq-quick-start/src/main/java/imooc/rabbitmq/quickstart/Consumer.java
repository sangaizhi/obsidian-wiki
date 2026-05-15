/**
 * @author: saz
 * @date 2019/3/30 22:22
 */
package imooc.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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

        // 声明(创建)一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);

        // 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        // 设置消费者监听的队列
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端：" + msg);
        }
        // 获取消息

    }
}
