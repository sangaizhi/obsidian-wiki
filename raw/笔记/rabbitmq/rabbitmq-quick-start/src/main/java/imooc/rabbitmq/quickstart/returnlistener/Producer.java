/**
 * @author: saz
 * @date 2019/4/6 23:14
 */
package imooc.rabbitmq.quickstart.returnlistener;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Return机制 的生产端
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
        String exchange = "test.return.exchange";
        channel.exchangeDeclare(exchange, "topic", true, false, null);
        // 声明 Routingkey
        String routingKey = "return.save";
        String errorRoutingKey = "abc.save";


        channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
            System.out.println("replyCode:" + replyCode);
            System.out.println("replyText:" + replyText);
            System.out.println("exchange:" + exchange1);
            System.out.println("routingKey:" + routingKey1);
            System.out.println("properties:" + properties);
            System.out.println("body:" + new String(body));
        });

        // 发送消息
        String msg = "Hello RabbitMQ Send Return Message!";
//        channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
        channel.basicPublish(exchange, errorRoutingKey, true, null, msg.getBytes());


    }
}
