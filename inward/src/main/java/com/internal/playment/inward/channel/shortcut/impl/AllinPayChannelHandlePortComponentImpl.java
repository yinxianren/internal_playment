package com.internal.playment.inward.channel.shortcut.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.cross.allinpay.ApiAllinPayOtherBusinessCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.tuple.Tuple4;
import com.internal.playment.inward.channel.shortcut.AllinPayChannelHandlePortComponent;
import com.internal.playment.inward.component.DbCommonRPCComponent;
import com.internal.playment.inward.service.channel.AllinPayChannelHandlePortService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AllinPayChannelHandlePortComponentImpl implements AllinPayChannelHandlePortComponent {

    @Reference(version = "${application.version}", group = "allinPay", timeout = 30000)
    private  ApiAllinPayOtherBusinessCrossComponent apiAllinPayOtherBusinessCrossComponent;
    @Autowired
    private  DbCommonRPCComponent dbCommonRPCComponent;
    @Autowired
    private  AllinPayChannelHandlePortService allinPayChannelHandlePortService;



    @Override
    public void asyncPayOderQuery(PayOrderInfoTable payOrderInfoTable) {
        final String bussType = "【收单异步主动通联查询】";
        try{
            //获取进件信息
            RegisterCollectTable registerCollectTable = dbCommonRPCComponent.apiRegisterCollectService.getOne(
                    new RegisterCollectTable()
                            .setBussType(BusinessTypeEnum.b3.getBusiType())
                            .setStatus(StatusEnum._0.getStatus())
                            .setPlatformOrderId(payOrderInfoTable.getRegPlatformOrderId()));
            if(isNull(registerCollectTable))
                throw  new Exception(format("%s-->查询进件信息，根据订单保存进件平台号：%s，查询结果为空",bussType,payOrderInfoTable.getRegPlatformOrderId()));
            //获取绑卡信息
            MerchantCardTable merchantCardTable = dbCommonRPCComponent.apiMerchantCardService.getOne(
                    new MerchantCardTable()
                            .setBussType(BusinessTypeEnum.b6.getBusiType())
                            .setStatus(StatusEnum._0.getStatus())
                            .setPlatformOrderId(payOrderInfoTable.getCardPlatformOrderId()));
            if(isNull(merchantCardTable))
                throw  new Exception(format("%s-->查询绑卡信息，根据订单保存绑卡平台号:%s，查询结果为空",bussType,payOrderInfoTable.getCardPlatformOrderId()));
            //获取通道信息
            ChannelInfoTable channelInfoTable = dbCommonRPCComponent.apiChannelInfoService.getOne(
                    new ChannelInfoTable()
                            .setStatus(StatusEnum._0.getStatus())
                            .setChannelId(payOrderInfoTable.getChannelId()));
            if(isNull(merchantCardTable))
                throw  new Exception(format("%s-->查询通道信息，根据订单保存通道id:%s，查询结果为空",bussType,payOrderInfoTable.getChannelId()));
            //封装请求信息
            RequestCrossMsgDTO requestCrossMsgDTO = allinPayChannelHandlePortService.packageRequestCrossMsgDTO(new Tuple4(registerCollectTable,merchantCardTable,channelInfoTable,payOrderInfoTable));
            //请求业务数据
            CrossResponseMsgDTO crossResponseMsgDTO = apiAllinPayOtherBusinessCrossComponent.queryByPayOrder(requestCrossMsgDTO);
            //判断业务场景
            switch (crossResponseMsgDTO.getCrossStatusCode()){
                case 0 :  allinPayChannelHandlePortService.successByPayOrder(payOrderInfoTable,crossResponseMsgDTO); break;
                case 1 :  allinPayChannelHandlePortService.fieldByPayOrder(payOrderInfoTable,crossResponseMsgDTO);   break;
                default:  allinPayChannelHandlePortService.otherByPayOrder(payOrderInfoTable,crossResponseMsgDTO);   break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void asyncTransOderQuery(TransOrderInfoTable transOrderInfoTable) {

    }
}
