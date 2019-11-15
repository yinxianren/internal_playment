package com.internal.playment.inward.component.mq;


import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.inward.channel.ChannelFacadeComponent;
import com.internal.playment.inward.component.ThreadPoolExecutorComponent;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AsyncOrderQueryConsumerComponent {

    private final ChannelFacadeComponent channelFacadeComponent;

    @JmsListener(destination="order.queue.async.query.payOrderInfoTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByPayOrder(PayOrderInfoTable payOrderInfoTable){
        ThreadPoolExecutorComponent.executorService.submit(()->{
            channelFacadeComponent.ChannelRoutingByPayOrder(payOrderInfoTable);
        });

    }


    @JmsListener(destination="order.queue.async.query.transOrderInfoTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByTransOrder(TransOrderInfoTable transOrderInfoTable){
        ThreadPoolExecutorComponent.executorService.submit(()->{
            channelFacadeComponent.ChannelRoutingByTransOrder(transOrderInfoTable);
        });

    }
}
