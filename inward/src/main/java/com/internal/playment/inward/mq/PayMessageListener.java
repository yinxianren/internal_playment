//package com.internal.playment.inward.mq;
//
//import com.rxh.anew.wallet.PayWalletComponent;
//import org.apache.activemq.command.ActiveMQObjectMessage;
//import org.apache.activemq.command.ActiveMQQueue;
//import org.apache.activemq.command.ActiveMQTopic;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//
//
//public class PayMessageListener implements MessageListener {
//
//    public final static String PAY_ODER_MSG = "payOderMsg";
//    public final static String TRANS_ODER_MSG = "transOderMsg";
//
//
//    @Autowired
//    private PayWalletComponent payWalletComponent;
//
//
//    @Override
//    public void onMessage(Message message) {
//        try {
//            Destination jmsDestination = message.getJMSDestination();
//            if (jmsDestination instanceof ActiveMQTopic) {
//
//
//            } else if (jmsDestination instanceof ActiveMQQueue) {
//                ActiveMQQueue activeMQQueue = (ActiveMQQueue) jmsDestination;
//                ActiveMQObjectMessage objectMessage = (ActiveMQObjectMessage) message;
//                switch (activeMQQueue.getQueueName()) {
//                    case PAY_ODER_MSG:
//                        payWalletComponent.payOrderWallet(objectMessage);
//                        break;
//                    case TRANS_ODER_MSG:
//                        payWalletComponent.transOrderWallet(objectMessage);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
//    }
//}