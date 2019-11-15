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


    @JmsListener(destination="application.queue.async.notify",  containerFactory="jmsListenerContainerQueue")
    public void receiveQueueByPayOrder(PayOrderInfoTable payOrderInfoTable){
        payWalletComponent.payOrderWallet(payOrderInfoTable);
    }

}
