//package com.internal.playment.inward.mq;
//
//
//import org.apache.activemq.Message;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//import org.springframework.stereotype.Component;
//
//import javax.jms.JMSException;
//import javax.jms.ObjectMessage;
//import javax.jms.Session;
//import java.io.Serializable;
//
///**
// *  描述： 订单队列，用于处理钱包
// * @author  panda
// * @date 20190721
// */
//@Component
//public class PayMessageSend {
//
//    @Autowired
//    private JmsTemplate  jmsTemplate;
//
//    /**
//     *  向队列发送收单订单
//     * @param object
//     */
//    public void sendObjectMessageToPayOderMsgMQ(final Serializable object) {
//        jmsTemplate.send( "payOderMsg", new MessageCreator() {
//            public Message createMessage(Session session) throws JMSException {
//                ObjectMessage objectMessage = session.createObjectMessage(object);
//                return (Message) objectMessage;
//            }
//        });
//    }
//
//
//    /**
//     *  向队列发送代付订单
//     * @param object
//     */
//    public void sendObjectMessageToTransOderMsgMQ(final Serializable object) {
//        jmsTemplate.send( "transOderMsg", new MessageCreator() {
//            public Message createMessage(Session session) throws JMSException {
//                ObjectMessage objectMessage = session.createObjectMessage(object);
//                return (Message) objectMessage;
//            }
//        });
//    }
//
//}
