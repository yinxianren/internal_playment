package com.internal.playment.task.component;

import lombok.AllArgsConstructor;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

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
     * @param queueName
     * @param object
     * @param time
     * @param <T>
     */
    public <T extends Serializable> void delaySend(String queueName,T object, Long time){
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        Destination destination = new ActiveMQQueue(queueName);
        // 获取连接工厂
        ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
        try {
            // 获取连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 获取session，true开启事务，false关闭事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            producer = session.createProducer(destination);
            producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
            ObjectMessage message = session.createObjectMessage(object);
            //设置延迟时间
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            // 发送消息
            producer.send(message);
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (producer != null){
                    producer.close();
                }
                if (session != null){
                    session.close();
                }
                if (connection != null){
                    connection.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    /**
     *
     * @param msg 广播信息
     */
    public void publish(String msg) {
        this.jmsTemplate.convertAndSend(this.payMsgTopic, msg);
    }
}
