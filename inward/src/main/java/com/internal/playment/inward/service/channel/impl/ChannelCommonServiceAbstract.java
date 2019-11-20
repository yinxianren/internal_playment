package com.internal.playment.inward.service.channel.impl;

import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.PayTreeMap;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple4;
import com.internal.playment.inward.component.DbCommonRPCComponent;
import com.internal.playment.inward.component.Md5Component;
import com.internal.playment.inward.component.mq.ActiveMqOrderProducerComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public abstract class ChannelCommonServiceAbstract {

    @Autowired
    protected ActiveMqOrderProducerComponent activeMqOrderProducerComponent;
    @Autowired
    protected DbCommonRPCComponent dbCommonRPCComponent;
    @Autowired
    protected Md5Component md5Component;

    @Value("${application.queue.pay-order}")
    protected String payOrder;
    @Value("${application.queue.trans-order}")
    protected String transOrder;
    @Value("${application.async-query.pay-order}")
    protected String asyncQueryPayOrder;
    @Value("${application.async-query.trans-order}")
    protected String asyncQueryTransOrder;
    @Value("${application.async-notify.order}")
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

    public RequestCrossMsgDTO packageRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple4<RegisterCollectTable, MerchantCardTable, ChannelInfoTable, PayOrderInfoTable> tuple4
                = (Tuple4<RegisterCollectTable, MerchantCardTable, ChannelInfoTable, PayOrderInfoTable>) tuple;
        return new RequestCrossMsgDTO()
                .setRegisterCollectTable(tuple4._1)
                .setMerchantCardTable(tuple4._2)
                .setChannelInfoTable(tuple4._3)
                .setPayOrderInfoTable(tuple4._4);
    }

}
