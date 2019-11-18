package com.internal.playment.inward.notify;

import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.PayTreeMap;
import com.internal.playment.common.inner.PayUtil;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.inward.component.DbCommonRPCComponent;
import com.internal.playment.inward.component.Md5Component;
import com.internal.playment.inward.component.mq.ActiveMqOrderProducerComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public abstract class NotifyCommonServiceAbstract implements PayUtil, NewPayAssert {

    @Autowired
    protected ActiveMqOrderProducerComponent activeMqOrderProducerComponent;
    @Autowired
    protected DbCommonRPCComponent dbCommonRPCComponent;
    @Autowired
    protected Md5Component md5Component;

    @Value("${application.sync-notify}")
    protected String asyncNotify;



    public Map<String, Object> packageAsyncNotifyMapObj(AsyncNotifyTable asyncNotifyTable, MerchantInfoTable merchantInfoTable, String remark) {
        PayTreeMap map =   new PayTreeMap<String, Object>()
                .lput("merId",asyncNotifyTable.getMerchantId())
                .lput("merOrderId",asyncNotifyTable.getMerOrderId())
                .lput("platformOrderId",asyncNotifyTable.getPlatformOrderId())
                .lput("terMerId",asyncNotifyTable.getTerminalMerId())
                .lput("amount",asyncNotifyTable.getAmount())
                .lput("status",asyncNotifyTable.getStatus())
                .lput("msg",remark);

        return map
                .lput("signMsg",md5Component.getMd5SignWithKey(map, merchantInfoTable.getSecretKey()));
    }

}
