/**
 * @author: saz
 * @date 2019/4/7 20:53
 */
package imooc.rabbitmq.quickstart.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 自定义消费者
 */
public class CustomConsumer extends DefaultConsumer {

    private Channel channel;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public CustomConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    /**
     * 自定义消费消息的方式
     *
     * @param consumerTag
     * @param envelope
     * @param properties
     * @param body
     * @throws IOException
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
//        System.out.println("consumerTag:" + consumerTag);
//        System.out.println("envelope:" + envelope);
//        System.out.println("properties:" + properties);
        System.out.println("body:" + new String(body));

        int num = (int) properties.getHeaders().get("num");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (num == 0) {
            // 发送 nack， 第三个参数表示重回队列，重回队列会把消费失败的消息添加到队列的尾端
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        }else{
            channel.basicAck(envelope.getDeliveryTag(), false);
        }

    }
}
