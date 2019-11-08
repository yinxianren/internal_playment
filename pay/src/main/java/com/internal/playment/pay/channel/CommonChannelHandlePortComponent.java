package com.internal.playment.pay.channel;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.tuple.Tuple2;

public interface CommonChannelHandlePortComponent {



    /**
     * 商户基础信息登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     *  银行卡登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     *  商户业务开通接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     * 绑卡申请接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     *  绑卡短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     *  绑卡确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     * 支付下单申请
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     *  支付短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     * 支付交易确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;

    /**
     * 付款
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception;





    /**
     *  收单业务
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @return
     */
    Tuple2 channelDifferBusinessHandleByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);

    /**
     *  代付业务
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @return
     */
    Tuple2  channelDifferBusinessHandleByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);

}
