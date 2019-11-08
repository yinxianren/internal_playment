package com.internal.playment.api.cross.allinpay;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;

import java.text.ParseException;

public interface ApiAllinPayOtherBusinessCrossComponent {

    CrossResponseMsgDTO queryByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO) throws ParseException;

    CrossResponseMsgDTO queryByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO);

    CrossResponseMsgDTO unBondCard(RequestCrossMsgDTO requestCrossMsgDTO);

}
