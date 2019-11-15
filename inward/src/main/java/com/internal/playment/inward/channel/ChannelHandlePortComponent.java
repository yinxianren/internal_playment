package com.internal.playment.inward.channel;

import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.PayUtil;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;

public interface ChannelHandlePortComponent extends NewPayAssert, PayUtil {

    void  asyncPayOderQuery(PayOrderInfoTable payOrderInfoTable);

    void  asyncTransOderQuery(TransOrderInfoTable transOrderInfoTable);
}
