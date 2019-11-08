package com.internal.playment.api.cross;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

public interface ApiPayOrderCrossComponent {
    /**
     * 支付下单申请
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

    /**
     *  支付短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

    /**
     * 支付交易确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;
}