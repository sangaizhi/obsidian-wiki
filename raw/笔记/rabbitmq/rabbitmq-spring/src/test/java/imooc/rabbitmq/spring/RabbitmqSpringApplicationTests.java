package imooc.rabbitmq.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import imooc.rabbitmq.spring.entity.Order;
import imooc.rabbitmq.spring.entity.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    public RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
    }

    @Test
    public void testRabbitAdmin() {
        // 声明交换机
        rabbitAdmin.declareExchange(new DirectExchange("test.spring.direct.exchange", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.spring.topic.exchange", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.spring.fanout.exchange", false, false));

        // 声明交换机
        rabbitAdmin.declareQueue(new Queue("test.spring.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.spring.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.spring.fanout.queue", false));


        // 绑定 exchange 和 queue
        rabbitAdmin.declareBinding(new Binding("test.spring.direct.queue", Binding.DestinationType.QUEUE,
                "test.spring.direct.exchange",
                "test.spring.direct", new HashMap<>()));
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test.spring.topic.queue", false))   // 直接创建队列
                        .to(new TopicExchange("test.spring.topic.exchange", false, false))  // 直接创建交换机
                        .with("test.spring.topic")); // 建立绑定
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test.spring.fanout.queue", false))
                        .to(new FanoutExchange("test.spring.fanout.exchange")));

        // 清空队列
        rabbitAdmin.purgeQueue("test.spring.topic.queue", false);
    }

    @Test
    public void testSendMessage() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述..");
        messageProperties.getHeaders().put("type", "消息烈性");
        String messageBody = "Hello RabbitMQ!";
        Message message = new Message(messageBody.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("test.spring.bean.exchange001", "test.spring.bean.001.first001", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("processor");
                message.getMessageProperties().getHeaders().put("desc", "processor信息描述");
                message.getMessageProperties().getHeaders().put("attr", "processor属性");
                return message;
            }
        });
    }

    @Test
    public void testSendMessage2() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        String messageBody = "Hello RabbitMQ  text/plain !";
        Message message = new Message(messageBody.getBytes(), messageProperties);
        rabbitTemplate.send("test.spring.bean.exchange001", "test.spring.bean.001.first001", message);

        rabbitTemplate.convertAndSend("test.spring.bean.exchange002", "test.spring.bean.002.first001", "Hello 002 message");
        rabbitTemplate.convertAndSend("test.spring.bean.exchange001", "test.spring.bean.003.first001", "Hello 003 message");
    }

    @Test
    public void testSendQueueMappingMessageListenerAdapter() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message2 = new Message("Hello MessageListenerAdapter > 2 002 message".getBytes(), messageProperties);
        Message message3 = new Message("Hello MessageListenerAdapter > 2 003 message".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("test.spring.bean.exchange002", "test.spring.bean.002.first001", message2);
        rabbitTemplate.convertAndSend("test.spring.bean.exchange001", "test.spring.bean.003.first001", message3);
    }


    /**
     * 测试 json  格式的消息监听适配器
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testSendJsonMessageListenerAdapter() throws JsonProcessingException {

        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("消息订单内容");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);

//        System.out.println("order 的 json 信息：" + json);

        MessageProperties messageProperties = new MessageProperties();

        // 这里一定需要修改为 application/json
        messageProperties.setContentType("application/json");

        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("test.spring.mla.json.exchange", "test.spring.mla.json.routingKey", message);

    }

    /**
     * 测试 json  格式消息转换为 Java 对象的监听
     * @throws JsonProcessingException
     */
    @Test
    public void testSendJson2JavaTypeMessageListenerConverter() throws JsonProcessingException {
        Order order = new Order();
        order.setId("002");
        order.setName("消息订单");
        order.setContent("消息订单内容");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);

//        System.out.println("order 的 json 信息：" + json);

        MessageProperties messageProperties = new MessageProperties();

        // 这里一定需要修改为 application/json
        messageProperties.setContentType("application/json");
        // 设置转换到的目标对象对象
        messageProperties.getHeaders().put("__TypeId__", "imooc.rabbitmq.spring.entity.Order");

        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("test.spring.mla.json.exchange", "test.spring.mla.json.routingKey", message);
    }

    /**
     * 测试 json  格式消息转换为 Java 对象多映射的监听
     * @throws JsonProcessingException
     */
    @Test
    public void testSendJson2MultipleJavaTypeMessageListenerConverter() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        Order order = new Order();
        order.setId("002");
        order.setName("消息订单");
        order.setContent("消息订单内容");

        String jsonOrder = objectMapper.writeValueAsString(order);

//        System.out.println("order 的 json 信息：" + json);

        MessageProperties orderMessageProperties = new MessageProperties();

        // 这里一定需要修改为 application/json
        orderMessageProperties.setContentType("application/json");
        // 设置转换到的目标对象对象，这里注意值是一个字符串，这个字符串要 adapter 中的 MessageConverter设置的可转换对象中
        /*
        例如：
         Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("order", Order.class);
        idClassMapping.put("packaged", Packaged.class);

        javaTypeMapper.setIdClassMapping(idClassMapping);
         */
        orderMessageProperties.getHeaders().put("__TypeId__", "order");

        Message orderMessage = new Message(jsonOrder.getBytes(), orderMessageProperties);
        rabbitTemplate.convertAndSend("test.spring.mla.json.exchange", "test.spring.mla.json.routingKey", orderMessage);

        Packaged packaged = new Packaged();
        packaged.setId("0002");
        packaged.setName("包裹消息");
        packaged.setDescription("包裹描述信息");
        String jsonPackaged = objectMapper.writeValueAsString(packaged);

        MessageProperties packagedMessageProperties = new MessageProperties();
        packagedMessageProperties.setContentType("application/json");

        packagedMessageProperties.getHeaders().put("__TypeId__", "packaged");
        Message packagedMessage = new Message(jsonPackaged.getBytes(), packagedMessageProperties);
        rabbitTemplate.convertAndSend("test.spring.mla.json.exchange", "test.spring.mla.json.routingKey", packagedMessage);


    }

    @Test
    public void testSendExtMessageListenerConverter() throws IOException {
//        byte[] body = Files.readAllBytes(Paths.get("C:\\Users\\sangaizhi\\Pictures", "test.jpg"));
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("image/jpg");
//        messageProperties.getHeaders().put("extName", "png");
//
//        Message message = new Message(body,messageProperties );
//        rabbitTemplate.send("test.spring.mla.image.exchange","test.spring.mla.image.routingKey" ,message);

        byte[] body = Files.readAllBytes(Paths.get("C:\\Users\\sangaizhi\\Downloads", "test.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");

        Message message = new Message(body,messageProperties );
        rabbitTemplate.send("test.spring.mla.pdf.exchange","test.spring.mla.pdf.routingKey" ,message);


    }




}
