package com.internal.playment.inward.component.mq;


import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.inward.wallet.PayWalletComponent;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class OrderConsumerComponent {

    private final PayWalletComponent payWalletComponent;


    @JmsListener(destination="order.queue.payOrderInfoTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByPayOrder(PayOrderInfoTable payOrderInfoTable){
        payWalletComponent.payOrderWallet(payOrderInfoTable);
    }

    @JmsListener(destination="order.queue.transOrderInfoTable",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByTransOrder(TransOrderInfoTable transOrderInfoTable){
        payWalletComponent.transOrderWallet(transOrderInfoTable);
    }

}
