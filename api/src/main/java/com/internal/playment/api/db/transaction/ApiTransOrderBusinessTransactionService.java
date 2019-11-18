package com.internal.playment.api.db.transaction;


import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;

import java.util.List;

public interface ApiTransOrderBusinessTransactionService {

    void updateByPayOrderCorrelationInfo(TransOrderInfoTable transOrderInfoTable, List<PayOrderInfoTable> payOrderInfoTableList);

    void updateAndSaveTransOderMsg(TransOrderInfoTable transOrderInfoTable, TransOrderInfoTable transOrderInfoTableHedging);
}
