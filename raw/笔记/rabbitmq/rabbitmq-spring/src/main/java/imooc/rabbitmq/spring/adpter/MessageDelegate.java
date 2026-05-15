/**
 * @author: saz
 * @date 2019/4/13 20:22
 */
package imooc.rabbitmq.spring.adpter;

import imooc.rabbitmq.spring.entity.Order;
import imooc.rabbitmq.spring.entity.Packaged;

import java.io.File;
import java.util.Map;

public class MessageDelegate {

//    /**
//     * 默认监听的方法名
//     * @param messageBody 消息内容
//     */
//    public void handleMessage(byte[] messageBody) {
//        System.out.println("handleMessage > byte[]:" + new String(messageBody));
//    }
//    /**
//     * 默认监听的方法名
//     * @param messageBody 消息内容
//     */
//    public void handleMessage(String messageBody) {
//        System.out.println("handleMessage > String:" + messageBody);
//    }
//
//    /**
//     * 自定义监听的方法名
//     * @param messageBody 消息内容
//     */
//    public void consumeMessage(byte[] messageBody) {
//        System.out.println("consumeMessage > byte[]:" + new String(messageBody));
//    }
//    /**
//     * 自定义监听的方法名
//     * @param messageBody 消息内容
//     */
//    public void consumeMessage(String messageBody) {
//        System.out.println("consumeMessage > String[]:" + messageBody);
//    }
//

    /**
     * 指定队列的消息监听
     *
     * @param messageBody 消息内容
     */
    public void handleQueue002(String messageBody) {
        System.out.println("handleQueue002 > Strin:" + messageBody);
    }

    /**
     * 指定队列的消息监听
     *
     * @param messageBody 消息内容
     */
    public void handleQueue003(String messageBody) {
        System.out.println("handleQueue003 > String:" + messageBody);
    }


    /**
     * 消息监听，用于监听 json 格式的消息
     *
     * @param messageBody 消息内容
     */
    public void consumeMessage(Map messageBody) {
        System.out.println("consumeMessage > Map:" + messageBody);
    }


    /**
     * 消息监听，用于监听可转换为Java对象的消息
     *
     * @param order 消息内容
     */
    public void consumeMessage(Order order) {
        System.out.println("consumeMessage > Order:" + order.toString());
    }


    /**
     * 消息监听，用于监听可转换为Java对象的消息
     *
     * @param packaged 消息内容
     */
    public void consumeMessage(Packaged packaged) {
        System.out.println("consumeMessage > Packaged:" + packaged.toString());
    }

    /**
     * 消息监听，用于监听可转换为文件的消息
     * @param file
     */
    public void consumeMessage(File file){
        System.out.println("consumeMessage > file:" + file.getName());
    }
}


