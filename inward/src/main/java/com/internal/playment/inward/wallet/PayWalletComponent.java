package com.internal.playment.inward.wallet;

import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;

public interface PayWalletComponent {

    void payOrderWallet(PayOrderInfoTable payOrderInfoTable);

    void transOrderWallet(TransOrderInfoTable transOrderInfoTable);

}
