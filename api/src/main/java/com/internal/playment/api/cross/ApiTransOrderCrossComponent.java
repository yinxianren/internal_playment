package com.internal.playment.api.cross;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

public interface ApiTransOrderCrossComponent {
    /**
     * 付款
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

}