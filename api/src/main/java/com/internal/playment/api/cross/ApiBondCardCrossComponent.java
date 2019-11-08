package com.internal.playment.api.cross;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

public interface ApiBondCardCrossComponent {
    /**
     * 绑卡申请接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

    /**
     *  绑卡短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

    /**
     *  绑卡确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;
}