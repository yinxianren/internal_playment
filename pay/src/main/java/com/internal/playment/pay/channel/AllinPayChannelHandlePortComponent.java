package com.internal.playment.pay.channel;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;

public interface AllinPayChannelHandlePortComponent extends  CommonChannelHandlePortComponent{

    /**
     * 快捷免验证支付
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);



}
