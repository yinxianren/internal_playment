package com.internal.playment.api.cross;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

public interface ApiIntoPiecesOfInformationCrossComponent {

    /**
     * 商户基础信息登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

    /**
     *  银行卡登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

    /**
     *  商户业务开通接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;
}
