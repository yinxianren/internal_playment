package com.internal.playment.pay.component;

import lombok.AllArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.Topic;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/9
 * Time: 下午1:36
 * Description:
 */
@AllArgsConstructor
@Component
public class ActiveMqOrderProducerComponent {

    private  final JmsMessagingTemplate jmsTemplate; //用来发送消息到broker的对象
    private final Topic payMsgTopic;

    /**
     *
     * @param queueName 队列名称
     * @param object 对象
     */
    public void sendMessage(String queueName,Object object){
        Destination destination = new ActiveMQQueue(queueName);
        jmsTemplate.convertAndSend(destination, object);
    }

    /**
     *
     * @param msg 广播信息
     */
    public void publish(String msg) {
        this.jmsTemplate.convertAndSend(this.payMsgTopic, msg);
    }
}
