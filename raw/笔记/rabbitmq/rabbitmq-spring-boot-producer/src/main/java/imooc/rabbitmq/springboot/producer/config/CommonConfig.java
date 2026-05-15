/**
 * @author: saz
 * @date 2019/4/18 21:08
 */
package imooc.rabbitmq.springboot.producer.config;

import imooc.rabbitmq.springboot.producer.producer.RabbitSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("{imooc.rabbitmq.springboot.*}")
public class CommonConfig {

    @Bean
    public RabbitSender rabbitSender(){
        return new RabbitSender();
    }
}
