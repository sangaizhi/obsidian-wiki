/**
 * @author: saz
 * @date 2019/3/30 22:22
 */
package imooc.rabbitmq.quickstart.confirm;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Confirm 确认消息的生产者
 */
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

        // 通过 connection 创建一个 channel
        Channel channel = connection.createChannel();

        // 指定消息的投递模式：消息的确认模式
        channel.confirmSelect();

        String exchange = "test-confirm-exchange";
        String routingKey = "confirm.test";

        // 发送消息
        String msg = "Hello RabbitMQ Send Confirm message!";
        channel.basicPublish(exchange,routingKey ,null ,msg.getBytes());

        // 添加一个确认监听

        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 成功执行的方法
             * @param deliveryTag 消息唯一的标签
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("ACK");
            }


            /**
             * 失败执行的方法
             * @param deliveryTag 消息唯一的标签
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("No ACK");
            }
        });
    }
}
