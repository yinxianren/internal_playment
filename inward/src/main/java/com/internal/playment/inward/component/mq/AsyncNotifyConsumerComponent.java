package com.internal.playment.inward.component.mq;


import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.inward.notify.OrderNotifyService;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AsyncNotifyConsumerComponent {

    private final OrderNotifyService orderNotifyService;

    @JmsListener(destination="order.queue.async.asyncNotifyTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByPayOrder(AsyncNotifyTable asyncNotifyTable){
        orderNotifyService.orderNotify(asyncNotifyTable);
    }
}
