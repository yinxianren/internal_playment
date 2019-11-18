package com.internal.playment.inward.service.channel.impl;

import com.alibaba.fastjson.JSON;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.inward.service.channel.AllinPayChannelHandlePortService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AllinPayChannelHandlePortServiceImpl extends ChannelCommonServiceAbstract implements AllinPayChannelHandlePortService {


    @Override
    public void successByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception {
        int payOrderStatus = payOrderInfoTable.getStatus();
        //说明与同步返回的结果是一样的；什么也不处理
        if( payOrderStatus == 7 ) { }
        //其他状态改为队列处理中
        else {
            payOrderInfoTable
                    .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                    .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                    .setUpdateTime(new Date())
                    .setStatus(StatusEnum._7.getStatus());
            dbCommonRPCComponent.apiPayOrderInfoService.updateByPrimaryKey(payOrderInfoTable);
            activeMqOrderProducerComponent.sendMessage(payOrder, payOrderInfoTable);//发生到钱包队列处理
        }
        //封装数据库保存对象
        AsyncNotifyTable asyncNotifyTable = packageAsyncNotifyTable(payOrderInfoTable,StatusEnum._0.getStatus());
        //保存信息
        dbCommonRPCComponent.apiAsyncNotifyService.save(asyncNotifyTable);
        //放到队列执行
        activeMqOrderProducerComponent.sendMessage(asyncNotify,asyncNotifyTable);
    }

    @Override
    public void fieldByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) {
        int payOrderStatus = payOrderInfoTable.getStatus();
        //同步状态为1，异步状态为1，一致什么也不处理
        if( payOrderStatus == 1){}
        // 同步状态为0，异步状态为1
        else if( payOrderStatus == 7 ){
          //做对冲，生成一个新的订单


        }else{//其他状态全部更新为失败
            payOrderInfoTable
                    .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                    .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                    .setUpdateTime(new Date())
                    .setStatus(StatusEnum._1.getStatus());
            //更新数据库表信息
            dbCommonRPCComponent.apiPayOrderInfoService.updateByPrimaryKey(payOrderInfoTable);
        }
        //封装数据库保存对象
        AsyncNotifyTable asyncNotifyTable = packageAsyncNotifyTable(payOrderInfoTable,StatusEnum._1.getStatus());
        //保存信息
        dbCommonRPCComponent.apiAsyncNotifyService.save(asyncNotifyTable);
        //放到队列执行
        activeMqOrderProducerComponent.sendMessage(asyncNotify,asyncNotifyTable);
    }


    @Override
    public void otherByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) {
        activeMqOrderProducerComponent.delaySend(asyncQueryPayOrder,payOrderInfoTable,10000L);
    }


    private   AsyncNotifyTable packageAsyncNotifyTable(PayOrderInfoTable payOrderInfoTable,int status){
        AsyncNotifyTable ant =  new AsyncNotifyTable()
                .setMerchantId(payOrderInfoTable.getMerchantId())
                .setTerminalMerId(payOrderInfoTable.getTerminalMerId())
                .setMerOrderId(payOrderInfoTable.getMerOrderId())
                .setPlatformOrderId(payOrderInfoTable.getCardPlatformOrderId())
                .setAmount(payOrderInfoTable.getAmount())
                .setOrderStatus(status)
                .setNotifyCount(1)
                .setStatus(1)
                .setNotifyUrl(payOrderInfoTable.getNotifyUrl())
                .setRespResult(null)
                .setUpdateTime(new Date())
                .setCreateTime(new Date());
        synchronized(this){
            ant.setId(System.currentTimeMillis());
        }
        return ant;

    }

}
