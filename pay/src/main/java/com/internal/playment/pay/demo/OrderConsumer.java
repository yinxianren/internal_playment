package com.internal.playment.pay.demo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    // 接收 order.queue 队列的消息
    @JmsListener(destination="order.queue",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueue(String text){
        System.out.println("OrderConsumer收到的报文为:"+text);
    }
}
