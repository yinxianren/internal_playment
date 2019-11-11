package com.internal.playment.inward.component;


import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.inward.wallet.PayWalletComponent;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderConsumerComponent {

    private final PayWalletComponent payWalletComponent;

    // 接收 order.queue 队列的消息
    @JmsListener(destination="order.queue.payOrderInfoTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByPayOrder(PayOrderInfoTable payOrderInfoTable){
        payWalletComponent.payOrderWallet(payOrderInfoTable);
    }

    // 接收 order.queue 队列的消息
    @JmsListener(destination="order.queue.transOrderInfoTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByTransOrder(TransOrderInfoTable transOrderInfoTable){
        payWalletComponent.transOrderWallet(transOrderInfoTable);
    }
}
