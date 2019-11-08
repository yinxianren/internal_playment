package com.internal.playment.api.cross.sicpay;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

public interface ApiSicPayOtherBusinessCrossComponent {
    CrossResponseMsgDTO queryBondCard(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;
}
