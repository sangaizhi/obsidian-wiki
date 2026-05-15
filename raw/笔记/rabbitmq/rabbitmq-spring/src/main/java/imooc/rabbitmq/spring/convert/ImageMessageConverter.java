package imooc.rabbitmq.spring.convert;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;


public class ImageMessageConverter implements MessageConverter {

    @Override
    public org.springframework.amqp.core.Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("Image MessageConverter");
        Object extNameObject = message.getMessageProperties().getHeaders().get("extName");
        String extName = null == extNameObject ? "jpg" : extNameObject.toString();

        byte[] body = message.getBody();
        String fileName = UUID.randomUUID().toString();

        String path = "d://010_test/" + fileName + "." + extName;
        File file = new File(path);

        try {
            Files.copy(new ByteArrayInputStream(body), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
