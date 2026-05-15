package imooc.rabbitmq.spring.config;

import imooc.rabbitmq.spring.adpter.MessageDelegate;
import imooc.rabbitmq.spring.convert.ImageMessageConverter;
import imooc.rabbitmq.spring.convert.PDFMessageConverter;
import imooc.rabbitmq.spring.convert.TextMessageConvert;
import imooc.rabbitmq.spring.entity.Order;
import imooc.rabbitmq.spring.entity.Packaged;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Rabbit MQ 配置
 *
 * @author saz
 * @date 2019/4/10 20:42
 */
@Configuration
@ComponentScan({"imooc.rabbitmq.spring.*"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("www.sangaizhi.cn:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1、设置交换机类型
     * 2、将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无 RoutingKey 的概念
     * HeadersExchange: 通过添加属性 key-value 匹配
     * DirectExchange: 按照 routingkey 分发到指定队列
     * TopicExchange: 多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("test.spring.bean.exchange001");
    }

    @Bean
    public Queue queue001() {
        return new Queue("test.spring.bean.queue001");
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("test.spring.bean.001.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("test.spring.bean.exchange002");
    }

    @Bean
    public Queue queue002() {
        return new Queue("test.spring.bean.queue002", true);
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("test.spring.bean.002.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("test.spring.bean.queue003", true);
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("test.spring.bean.003.*");
    }


    @Bean
    public Queue imageQueue() {
        return new Queue("test.spring.bean.queue_image", true);
    }

    @Bean
    public Queue pdfQueue() {
        return new Queue("test.spring.bean.pdf_image", true);
    }

    @Bean
    public TopicExchange jsonMessageListenerAdapterExchange() {
        return new TopicExchange("test.spring.mla.json.exchange");
    }

    @Bean
    public Queue jsonMessageListenerAdapterQueue() {
        return new Queue("test.spring.mla.json.queue", true);
    }

    @Bean
    public Binding jsonMessageListenerAdapterBinding() {
        return BindingBuilder.bind(jsonMessageListenerAdapterQueue()).to(jsonMessageListenerAdapterExchange()).with("test.spring.mla.json.routingKey");
    }

    @Bean
    public TopicExchange imageMessageListenerAdapterExchange() {
        return new TopicExchange("test.spring.mla.image.exchange");
    }

    @Bean
    public Queue imageMessageListenerAdapterQueue() {
        return new Queue("test.spring.mla.image.queue", true);
    }

    @Bean
    public Binding imageMessageListenerAdapterBinding() {
        return BindingBuilder.bind(imageMessageListenerAdapterQueue()).to(imageMessageListenerAdapterExchange()).with("test.spring.mla.image.routingKey");
    }

    @Bean
    public TopicExchange pdfMessageListenerAdapterExchange() {
        return new TopicExchange("test.spring.mla.pdf.exchange");
    }

    @Bean
    public Queue pdfMessageListenerAdapterQueue() {
        return new Queue("test.spring.mla.pdf.queue", true);
    }

    @Bean
    public Binding pdfMessageListenerAdapterBinding() {
        return BindingBuilder.bind(pdfMessageListenerAdapterQueue()).to(pdfMessageListenerAdapterExchange()).with("test.spring.mla.pdf.routingKey");
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 监听队列
        container.setQueues(queue001(), queue002(), queue003(), imageQueue(), pdfQueue(), jsonMessageListenerAdapterQueue(),
                imageMessageListenerAdapterQueue(), pdfMessageListenerAdapterQueue());
        // 当前消费者的数量
        container.setConcurrentConsumers(1);
        // 最大消费者的数量
        container.setMaxConcurrentConsumers(5);

        // 重回队列
        container.setDefaultRequeueRejected(false);
        // 签收机制
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        // 消费端 tag 生产策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

        // 监听器方式
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                System.out.println("消费者：" + msg);
//            }
//        });

        /**
         * 适配器方式１，默认处理消息的方式是 handleMessage,当然该方法的名字是可以自定义的。
         * 针对不同的消息，可以添加一个消息转换器转换消息

         MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
         adapter.setDefaultListenerMethod("consumeMessage");
         adapter.setMessageConverter(new TextMessageConvert());
         container.setMessageListener(adapter);'
         */

        /**
         * 适配器方式2，我们队列名称和处理方法的名称也可以进行一一匹配
         */
//         MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//         adapter.setMessageConverter(new TextMessageConvert());
//         Map<String, String> queueOrTagToMethodName = new HashMap<>();
//         queueOrTagToMethodName.put("test.spring.bean.queue002", "handleQueue002");
//         queueOrTagToMethodName.put("test.spring.bean.queue003", "handleQueue003");
//         adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//         container.setMessageListener(adapter);


        // 支持 json 格式的转换器
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//
//        container.setMessageListener(adapter);


        // 支持 json 到 Java 对象转换的 json 转换器
        // Jackson2JsonMessageConverter 和 DefaultJackson2JavaTypeMapper一起使用
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        jackson2JsonMessageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//
//        container.setMessageListener(adapter);


        // 支持 json 到 Java 对象多映射转换的 json 转换器
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        // 定义可转换的 Java 对象类型，
//        Map<String, Class<?>> idClassMapping = new HashMap<>();
//        idClassMapping.put("order", Order.class);
//        idClassMapping.put("packaged", Packaged.class);
//
//        javaTypeMapper.setIdClassMapping(idClassMapping);
//
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//
//        container.setMessageListener(adapter);

        // 支持多种类型的转换器

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        TextMessageConvert textMessageConvert = new TextMessageConvert();
        converter.addDelegate("text", textMessageConvert);
        converter.addDelegate("html/text", textMessageConvert);
        converter.addDelegate("xml/text", textMessageConvert);
        converter.addDelegate("text/plain", textMessageConvert);

        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json", jsonMessageConverter);
        converter.addDelegate("application/json", jsonMessageConverter);

        ImageMessageConverter imageMessageConverter = new ImageMessageConverter();
        converter.addDelegate("image/jpg", imageMessageConverter);
        converter.addDelegate("image", imageMessageConverter);

        PDFMessageConverter pdfMessageConverter = new PDFMessageConverter();
        converter.addDelegate("application/pdf", pdfMessageConverter);

        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);

        return container;
    }
}
