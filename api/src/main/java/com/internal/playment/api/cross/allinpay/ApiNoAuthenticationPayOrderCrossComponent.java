package com.internal.playment.api.cross.allinpay;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

public interface ApiNoAuthenticationPayOrderCrossComponent {
    /**
     * 快捷免验证支付
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO);
}