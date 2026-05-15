/**
 * @author: saz
 * @date 2019/4/18 21:08
 */
package imooc.rabbitmq.springboot.consumer.config;

import imooc.rabbitmq.springboot.consumer.consumer.RabbitReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("{imooc.rabbitmq.springboot.*}")
public class CommonConfig {


    @Bean
    public RabbitReceiver rabbitReceiver(){
        return new RabbitReceiver();
    }

}
