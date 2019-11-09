package com.internal.playment.pay.demo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicSub {


    @JmsListener(destination="pay.msg.topic",containerFactory="jmsListenerContainerTopic")
    public void receive1(String text){
        System.out.println("topic.queue 消费者:receive1="+text);
    }


    @JmsListener(destination="pay.msg.topic",containerFactory="jmsListenerContainerTopic")
    public void receive2(String text){
        System.out.println("topic.queue 消费者:receive2="+text);
    }


    @JmsListener(destination="pay.msg.topic",containerFactory="jmsListenerContainerTopic")
    public void receive3(String text){
        System.out.println("topic.queue 消费者:receive3="+text);
    }


}