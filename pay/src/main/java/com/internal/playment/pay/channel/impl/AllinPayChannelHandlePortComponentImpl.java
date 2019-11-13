package com.internal.playment.pay.channel.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.internal.playment.api.cross.ApiBondCardCrossComponent;
import com.internal.playment.api.cross.ApiIntoPiecesOfInformationCrossComponent;
import com.internal.playment.api.cross.ApiPayOrderCrossComponent;
import com.internal.playment.api.cross.ApiTransOrderCrossComponent;
import com.internal.playment.api.cross.allinpay.ApiNoAuthenticationPayOrderCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.pay.channel.AllinPayChannelHandlePortComponent;
import com.internal.playment.pay.component.ActiveMqOrderProducerComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午5:09
 * Description:
 */

@Component
public class AllinPayChannelHandlePortComponentImpl implements AllinPayChannelHandlePortComponent {

    @Value("${application.queue.pay-order}")
    private String payOrder;
    @Value("${application.queue.trans-order}")
    private String transOrder;

    @Reference(version = "${application.version}", group = "allinPay", timeout = 30000)
    private ApiBondCardCrossComponent apiBondCardCrossComponent;
    @Reference(version = "${application.version}", group = "allinPay", timeout = 30000)
    private ApiIntoPiecesOfInformationCrossComponent apiIntoPiecesOfInformationCrossComponent;
    @Reference(version = "${application.version}", group = "allinPay", timeout = 30000)
    private ApiPayOrderCrossComponent apiPayOrderCrossComponent;
    @Reference(version = "${application.version}", group = "allinPay", timeout = 30000)
    private ApiTransOrderCrossComponent apiTransOrderCrossComponent;
    @Reference(version = "${application.version}", group = "allinPay", timeout = 30000)
    private ApiNoAuthenticationPayOrderCrossComponent apiNoAuthenticationPayOrderCrossComponent;
    @Autowired
    private ActiveMqOrderProducerComponent ActiveMqOrderProducerComponent;


    @Override
    public CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiIntoPiecesOfInformationCrossComponent.addCusInfo(requestCrossMsgDTO);
    }
    @Override
    public CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiIntoPiecesOfInformationCrossComponent.bankCardBind(requestCrossMsgDTO);
    }
    @Override
    public CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiIntoPiecesOfInformationCrossComponent.serviceFulfillment(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiBondCardCrossComponent.bondCardApply(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiBondCardCrossComponent.reGetBondCode(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiBondCardCrossComponent.confirmBondCard(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiPayOrderCrossComponent.payApply(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiPayOrderCrossComponent.getPayCode(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiPayOrderCrossComponent.confirmPay(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) throws Exception {
        return apiTransOrderCrossComponent.payment(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return apiNoAuthenticationPayOrderCrossComponent.exemptCodePay(requestCrossMsgDTO);
    }

    public Tuple2 channelDifferBusinessHandleByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO) {
        PayOrderInfoTable payOrderInfoTable = requestCrossMsgDTO.getPayOrderInfoTable();
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus())
            ActiveMqOrderProducerComponent.sendMessage(payOrder,payOrderInfoTable);
        return null;
    }

    public Tuple2 channelDifferBusinessHandleByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO) {
        TransOrderInfoTable transOrderInfoTable = requestCrossMsgDTO.getTransOrderInfoTable();
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus())
            ActiveMqOrderProducerComponent.sendMessage(transOrder,transOrderInfoTable);
        return null;
    }


}
