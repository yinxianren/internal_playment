package com.internal.playment.inward.service.channel;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;

public interface AllinPayChannelHandlePortService extends ChannelCommonServiceInterface{



    void successByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception;

    void otherByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO);

    void fieldByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception;

    void successByTransOrder(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception;

    void fieldByTransOrder(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception;

    void otherByTransOrder(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO);
}
