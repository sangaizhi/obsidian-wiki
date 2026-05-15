/**
 * @author: saz
 * @date 2019/3/31 0:10
 */
package imooc.rabbitmq.quickstart.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer4TopicExchange {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 创建一个 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();

        // 设置连接属性
        connectionFactory.setHost("129.211.100.215");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        // 是否支持自动重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        // 重连间隔时间,单位毫秒
        connectionFactory.setNetworkRecoveryInterval(3000);
        // 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        // 通过 connection 创建一个 Channel
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        // 1. 使用 '#' 通配符
//        String routingKey = "user.#";
        // 2. 使用 '*' 通配符
        // 同一个队列在将 routing key 从 '#' 通配符换成 '*' 通配符之后，如果没有解绑之前的 '#' 通配符，
        // 那么这个队列就会绑定两个 routing key
        String routingKey = "user.*";

        // 声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立交换机与队列之间的绑定
        channel.queueBind(queueName, exchangeName, routingKey);

        // 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        // 设置消费者监听的队列
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息：" + msg);
        }
        // 获取消息

    }
}
