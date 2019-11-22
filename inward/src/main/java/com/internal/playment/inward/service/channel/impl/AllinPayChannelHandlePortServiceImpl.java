package com.internal.playment.inward.service.channel.impl;

import com.alibaba.fastjson.JSON;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.inward.service.channel.AllinPayChannelHandlePortService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Service
public class AllinPayChannelHandlePortServiceImpl extends ChannelCommonServiceAbstract implements AllinPayChannelHandlePortService {


    @Override
    public void successByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception {
        int payOrderStatus = payOrderInfoTable.getStatus();
        //说明与同步返回的结果是一样的；什么也不处理
        if( payOrderStatus == 7 ) { }
        //其他状态改为队列处理中
        else payOrderInfoTable = this.successPayOderByOther(payOrderInfoTable,crossResponseMsgDTO);
        //封装数据库保存对象
        AsyncNotifyTable asyncNotifyTable = packageAsyncNotifyTable(payOrderInfoTable,StatusEnum._0.getStatus());
        //保存信息
        dbCommonRPCComponent.apiAsyncNotifyService.save(asyncNotifyTable);
        super.updatePayOrderNotifyStatus(payOrderInfoTable);
        //放到队列执行
        activeMqOrderProducerComponent.sendMessage(asyncNotify,asyncNotifyTable);
    }
    @Override
    public void successByTransOrder(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception {
        int payOrderStatus = transOrderInfoTable.getStatus();
        //说明与同步返回的结果是一样的；什么也不处理
        if( payOrderStatus == 7 ) { }
        //其他状态改为队列处理中
        else transOrderInfoTable = this.successTransOderByOther(transOrderInfoTable,crossResponseMsgDTO);
        //封装数据库保存对象
        AsyncNotifyTable asyncNotifyTable = packageAsyncNotifyTable(transOrderInfoTable,StatusEnum._0.getStatus());
        //保存信息
        dbCommonRPCComponent.apiAsyncNotifyService.save(asyncNotifyTable);
        super.updateTransOrderNotifyStatus(transOrderInfoTable);
        //放到队列执行
        activeMqOrderProducerComponent.sendMessage(asyncNotify,asyncNotifyTable);
    }

    /**
     *
     * @param transOrderInfoTable
     * @param crossResponseMsgDTO
     * @return
     */
    private TransOrderInfoTable successTransOderByOther(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) {
        transOrderInfoTable
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setUpdateTime(new Date())
                .setStatus(StatusEnum._7.getStatus());
        dbCommonRPCComponent.apiTransOrderInfoService.updateById(transOrderInfoTable);
        activeMqOrderProducerComponent.sendMessage(transOrder, transOrderInfoTable);//发生到钱包队列处理
        return transOrderInfoTable;
    }

    /**
     *
     * @param payOrderInfoTable
     * @param crossResponseMsgDTO
     * @return
     */
    private PayOrderInfoTable successPayOderByOther(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO){
        payOrderInfoTable
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setUpdateTime(new Date())
                .setStatus(StatusEnum._7.getStatus());
        dbCommonRPCComponent.apiPayOrderInfoService.updateByPrimaryKey(payOrderInfoTable);
        activeMqOrderProducerComponent.sendMessage(payOrder, payOrderInfoTable);//发生到钱包队列处理
        return payOrderInfoTable;
    }

    @Override
    public void fieldByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception {
        int payOrderStatus = payOrderInfoTable.getStatus();
        //同步状态为1，异步状态为1，一致什么也不处理
        if (payOrderStatus == StatusEnum._1.getStatus()){}
        // 同步状态为0，异步状态为1
        else if (payOrderStatus == StatusEnum._7.getStatus()) payOrderInfoTable = this.fieldPayOrderBySuccessToField(payOrderInfoTable,crossResponseMsgDTO);
            //其他状态全部更新为失败
        else  payOrderInfoTable = this.fieldPayOrderByOther(payOrderInfoTable,crossResponseMsgDTO);
        //封装数据库保存对象
        AsyncNotifyTable asyncNotifyTable = packageAsyncNotifyTable(payOrderInfoTable, StatusEnum._1.getStatus());
        //保存信息
        dbCommonRPCComponent.apiAsyncNotifyService.save(asyncNotifyTable);
        super.updatePayOrderNotifyStatus(payOrderInfoTable);
        //放到队列执行
        activeMqOrderProducerComponent.sendMessage(asyncNotify, asyncNotifyTable);

    }
    @Override
    public void fieldByTransOrder(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception {
        int payOrderStatus = transOrderInfoTable.getStatus();
        //同步状态为1，异步状态为1，一致什么也不处理
        if (payOrderStatus == StatusEnum._1.getStatus()){}
        // 同步状态为0，异步状态为1
        else if (payOrderStatus == StatusEnum._7.getStatus()) transOrderInfoTable = this.fieldTransOrderBySuccessToField(transOrderInfoTable,crossResponseMsgDTO);
        //封装数据库保存对象
        AsyncNotifyTable asyncNotifyTable = packageAsyncNotifyTable(transOrderInfoTable, StatusEnum._1.getStatus());
        //保存信息
        dbCommonRPCComponent.apiAsyncNotifyService.save(asyncNotifyTable);
        super.updateTransOrderNotifyStatus(transOrderInfoTable);
        //放到队列执行
        activeMqOrderProducerComponent.sendMessage(asyncNotify, asyncNotifyTable);
    }

    private TransOrderInfoTable fieldTransOrderBySuccessToField(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws Exception {
        TransOrderInfoTable transOrderInfoTableHedging = (TransOrderInfoTable) transOrderInfoTable.clone();
        //做对冲，生成一个新的订单
        synchronized (this) {
            transOrderInfoTableHedging.setId(System.currentTimeMillis())
                    .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B13" + System.currentTimeMillis());
        }
        transOrderInfoTableHedging.setAmount(transOrderInfoTable.getAmount().multiply(new BigDecimal(-1)))
                .setOutAmount(transOrderInfoTable.getOutAmount().multiply(new BigDecimal(-1)))
                .setBackFee(transOrderInfoTable.getBackFee().multiply(new BigDecimal(-1)))
//                .setTerRate(transOrderInfoTable.getTerRate().multiply(new BigDecimal(-1)))
                .setTerFee(transOrderInfoTable.getTerFee().multiply(new BigDecimal(-1)))
//                .setChannelRate(transOrderInfoTable.getChannelRate().multiply(new BigDecimal(-1)))
                .setChannelFee(transOrderInfoTable.getChannelFee().multiply(new BigDecimal(-1)))
//                .setAgentRate(transOrderInfoTable.getAgentRate().multiply(new BigDecimal(-1)))
                .setAgentFee(transOrderInfoTable.getAgentFee().multiply(new BigDecimal(-1)))
//                .setMerRate(transOrderInfoTable.getMerRate().multiply(new BigDecimal(-1)))
                .setMerFee(transOrderInfoTable.getMerFee().multiply(new BigDecimal(-1)))
                .setPlatformIncome(transOrderInfoTable.getPlatformIncome().multiply(new BigDecimal(-1)))
                .setBusiType(BusinessTypeEnum.b13.getBusiType())
                .setStatus(StatusEnum._7.getStatus())
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        //更新原来的订单
        transOrderInfoTable.setStatus(StatusEnum._12.getStatus())
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setUpdateTime(new Date());
        //采用事务更新
        dbCommonRPCComponent.apiTransOrderBusinessTransactionService.updateAndSaveTransOderMsg(transOrderInfoTable,transOrderInfoTableHedging);
        //发送到钱包队列处理
        activeMqOrderProducerComponent.sendMessage(transOrder, transOrderInfoTableHedging);
        return transOrderInfoTable;
    }

    /**
     * 同步返回成功，异步返回失败，需要做对冲
     * @param payOrderInfoTable
     * @param crossResponseMsgDTO
     * @return
     * @throws CloneNotSupportedException
     */
    private PayOrderInfoTable fieldPayOrderBySuccessToField(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) throws CloneNotSupportedException {

        PayOrderInfoTable payOrderInfoByHedging = (PayOrderInfoTable) payOrderInfoTable.clone();
        //做对冲，生成一个新的订单
        synchronized (this) {
            payOrderInfoByHedging.setId(System.currentTimeMillis())
                    .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B12" + System.currentTimeMillis());

        }
        payOrderInfoByHedging.setAmount(payOrderInfoTable.getAmount().multiply(new BigDecimal(-1)))
                .setInAmount(payOrderInfoTable.getInAmount().multiply(new BigDecimal(-1)))
//                .setPayFee(payOrderInfoTable.getPayFee().multiply(new BigDecimal(-1)))
                .setTerFee(payOrderInfoTable.getTerFee().multiply(new BigDecimal(-1)))
                .setChannelFee(payOrderInfoTable.getChannelFee().multiply(new BigDecimal(-1)))
                .setAgentFee(payOrderInfoTable.getAgentFee().multiply(new BigDecimal(-1)))
                .setMerFee(payOrderInfoTable.getMerFee().multiply(new BigDecimal(-1)))
                .setBussType(BusinessTypeEnum.b12.getBusiType())
                .setStatus(StatusEnum._7.getStatus())
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //更新原来的订单
        payOrderInfoTable.setStatus(StatusEnum._12.getStatus())
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setUpdateTime(new Date());
        //采用事务更新
        dbCommonRPCComponent.apiPayOrderBusinessTransactionService.updateAndSavePayOderMsg(payOrderInfoTable,payOrderInfoByHedging);
        //发送到钱包队列处理
        activeMqOrderProducerComponent.sendMessage(payOrder, payOrderInfoByHedging);
        return payOrderInfoTable;
    }

    private PayOrderInfoTable fieldPayOrderByOther(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO){
        payOrderInfoTable
                .setCrossRespResult(JSON.toJSONString(crossResponseMsgDTO))
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setUpdateTime(new Date())
                .setStatus(StatusEnum._1.getStatus());
        //更新数据库表信息
        dbCommonRPCComponent.apiPayOrderInfoService.updateByPrimaryKey(payOrderInfoTable);
        return payOrderInfoTable;
    }

    @Override
    public void otherByPayOrder(PayOrderInfoTable payOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) {
        activeMqOrderProducerComponent.delaySend(asyncQueryPayOrder,payOrderInfoTable,10000L);
    }

    @Override
    public void otherByTransOrder(TransOrderInfoTable transOrderInfoTable, CrossResponseMsgDTO crossResponseMsgDTO) {
        activeMqOrderProducerComponent.delaySend(asyncQueryTransOrder,transOrderInfoTable,10000L);
    }

    private   AsyncNotifyTable packageAsyncNotifyTable(Object obj ,int status) throws Exception {
        PayOrderInfoTable payOrderInfoTable = null;
        TransOrderInfoTable transOrderInfoTable = null;
        if(obj instanceof PayOrderInfoTable)  payOrderInfoTable = (PayOrderInfoTable) obj;
        else if(obj instanceof TransOrderInfoTable)  transOrderInfoTable = (TransOrderInfoTable) obj;
        else throw new Exception("没有匹配到合适的对象类型");

        AsyncNotifyTable ant =  new AsyncNotifyTable()
                .setMerchantId( obj instanceof PayOrderInfoTable ? payOrderInfoTable.getMerchantId() : transOrderInfoTable.getMerchantId())
                .setTerminalMerId( obj instanceof PayOrderInfoTable ? payOrderInfoTable.getTerminalMerId() : transOrderInfoTable.getTerminalMerId())
                .setMerOrderId( obj instanceof PayOrderInfoTable ? payOrderInfoTable.getMerOrderId() : transOrderInfoTable.getMerOrderId())
                .setPlatformOrderId(obj instanceof PayOrderInfoTable ? payOrderInfoTable.getPlatformOrderId() : transOrderInfoTable.getPlatformOrderId())
                .setAmount(obj instanceof PayOrderInfoTable ? payOrderInfoTable.getAmount() :  transOrderInfoTable.getAmount())
                .setNotifyUrl(obj instanceof PayOrderInfoTable ? payOrderInfoTable.getNotifyUrl() : transOrderInfoTable.getNotifyUrl())
                .setOrderStatus(status)
                .setNotifyCount(1)
                .setStatus(1)
                .setRespResult(null)
                .setUpdateTime(new Date())
                .setCreateTime(new Date());
        return ant;
    }

}
