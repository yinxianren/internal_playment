package com.internal.playment.inward.wallet.impl;

import com.alibaba.fastjson.JSON;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelDetailsTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.inward.component.DbCommonRPCComponent;
import com.internal.playment.inward.service.PayWalletService;
import com.internal.playment.inward.wallet.PayWalletComponent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午5:12
 * Description:
 */

@Component
public class PayWalletComponentComponentImpl implements PayWalletComponent, NewPayAssert {

    @Autowired
    private  PayWalletService payWalletService;
    @Autowired
    private  DbCommonRPCComponent dbCommonRPCComponent;
    private final Object  lock = new Object();

    @Override
    public void payOrderWallet( PayOrderInfoTable poi) {
        final String bussType = "快捷MQ队列--->收单业务钱包更新";
        InnerPrintLogObject ipo;
        try {
            //创建日志打印对象
            ipo = new InnerPrintLogObject(poi.getMerchantId(), poi.getTerminalMerId(),bussType);
            //获取商户信息
            MerchantInfoTable mit = payWalletService.getMerInfo(ipo);
            //获取商户产品费率
            MerchantRateTable mrt = payWalletService.getMerRate(ipo,poi.getProductId());
            //获取通道信息
            ChannelInfoTable cit = payWalletService.getChannelInfo(poi.getChannelId(),ipo);
            //获取代理商设置
            AgentMerchantSettingTable ams = payWalletService.getAgentMerSet(mit.getAgentMerchantId(),poi.getProductId(),ipo);
            synchronized (lock) {
                //判断订单是否有必要执行
                payWalletService.checkPayOrderOperability(poi,ipo);
                //获取商户钱包
                MerchantWalletTable mwt = payWalletService.getMerWallet(ipo);
                //更新商户钱包 ,保存商户钱包明细
                Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple = payWalletService.updateMerWalletByPayOrder(mwt, poi, mrt);
                //获取终端商户钱包
                TerminalMerchantsWalletTable tmw = payWalletService.getTerMerWallet(ipo);
                //更新终端商户钱包 保存终端商户钱包明细
                Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple = payWalletService.updateTerMerWalletByPayOrder(tmw, poi, mrt);
                //获取通道钱包
                ChannelWalletTable cwt = payWalletService.getChanWallet(poi.getChannelId(), ipo);
                //更新通道钱包 保存通道钱包明细
                Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple = payWalletService.updateChannelWalletByPayOrder(cwt, cit, poi, mrt);
                //获取代理商钱包
                AgentMerchantWalletTable amw = payWalletService.getAgentMerWallet(mit.getAgentMerchantId(), ipo);
                //更新代理商钱包 保存代理商钱包明细
                Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple = payWalletService.updateAgentMerWalletByPayOrder(amw, ams, poi);
                //更新订单状态，从队列处理中该为成功
                poi = poi.setStatus(StatusEnum._0.getStatus());
                //执行事务处理
                dbCommonRPCComponent.apiPayOrderBusinessTransactionService.updateOrSavePayOrderBussInfo(merWalletTuple, terMerWalletTuple, chanWalletTuple, agentMerWalletTuple, poi);
            }
        }catch (Exception e){
            e.printStackTrace();
            if( !isNull(poi) ){
                poi = poi.setStatus(StatusEnum._8.getStatus());
                dbCommonRPCComponent.apiPayOrderInfoService.updateByPrimaryKey(poi);
                dbCommonRPCComponent.apiSystemOrderTrackService.save( new SystemOrderTrackTable()
                        .setRequestPath(bussType)
                        .setPlatformPrintLog( e instanceof NewPayException ? ((NewPayException)e).getInnerPrintMsg() : e.getMessage())
                        .setId(null)
                        .setMerId(poi.getMerchantId())
                        .setMerOrderId(poi.getMerOrderId())
                        .setPlatformOrderId(poi.getPlatformOrderId())
                        .setAmount(poi.getAmount())
                        .setReturnUrl(null)
                        .setNoticeUrl(null)
                        .setTradeCode(StatusEnum._8.getStatus())
                        .setRequestMsg(StatusEnum._8.getRemark())
                        .setReferPath(" public void payOrderWallet( ActiveMQObjectMessage objectMessage) ")
                        .setResponseResult(JSON.toJSONString(poi))
                        .setTradeTime(new Date())
                        .setCreateTime(new Date()));
            }
        }
    }


    @Override
    public void transOrderWallet( TransOrderInfoTable toit ) {
        final String bussType = "快捷MQ队列--->代付业务钱包更新";
        InnerPrintLogObject ipo;
        try{
            //创建日志打印对象
            ipo = new InnerPrintLogObject(toit.getMerchantId(), toit.getTerminalMerId(),bussType);
            //获取商户信息
            MerchantInfoTable mit = payWalletService.getMerInfo(ipo);
            //获取商户产品费率
            MerchantRateTable mrt = payWalletService.getMerRate(ipo,toit.getProductId());
            //获取通道信息
            ChannelInfoTable  cit = payWalletService.getChannelInfo(toit.getChannelId(),ipo);
            //获取代理商设置
            AgentMerchantSettingTable ams = payWalletService.getAgentMerSet(mit.getAgentMerchantId(),toit.getProductId(),ipo);
            //获取收单订单信息
            List<PayOrderInfoTable> payOrderInfoTableList = payWalletService.getPayOrderInfo(toit,ipo);
            synchronized (lock) {
                //判断订单是否有必要执行
                payWalletService.checkTransOrderOperability(toit,ipo);
                //获取商户钱包
                MerchantWalletTable mwt = payWalletService.getMerWallet(ipo);
                //更新商户钱包 ,保存商户钱包明细
                Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple = payWalletService.updateMerWalletByTransOrder(mwt, toit, mrt);
                //获取终端商户钱包
                TerminalMerchantsWalletTable tmw = payWalletService.getTerMerWallet(ipo);
                //更新终端商户钱包 保存终端商户钱包明细
                Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple = payWalletService.updateTerMerWalletByTransOrder(tmw, toit, mrt);
                //获取通道钱包
                ChannelWalletTable cwt = payWalletService.getChanWallet(payOrderInfoTableList.get(0).getChannelId(), ipo);
                //更新通道钱包 保存通道钱包明细
                Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple = payWalletService.updateChannelWalletByTransOrder(cwt, cit, toit, mrt);
                //获取代理商钱包
                AgentMerchantWalletTable amw = payWalletService.getAgentMerWallet(mit.getAgentMerchantId(), ipo);
                //更新代理商钱包 保存代理商钱包明细
                Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple = payWalletService.updateAgentMerWalletByTransOrder(amw, ams, toit);
                //更新订单状态，从队列处理中该为成功
                toit = toit.setStatus(StatusEnum._0.getStatus());
                //执行事务处理
                dbCommonRPCComponent.apiPayOrderBusinessTransactionService.updateOrSaveTransOrderBussInfo(merWalletTuple, terMerWalletTuple, chanWalletTuple, agentMerWalletTuple, payOrderInfoTableList,toit);
            }
        }catch (Exception e){
            e.printStackTrace();
            if( !isNull(toit) ){
                toit = toit.setStatus(StatusEnum._8.getStatus());
                dbCommonRPCComponent.apiTransOrderInfoService.updateById(toit);
                dbCommonRPCComponent.apiSystemOrderTrackService.save( new SystemOrderTrackTable()
                        .setRequestPath(bussType)
                        .setPlatformPrintLog( e instanceof NewPayException ? ((NewPayException)e).getInnerPrintMsg() : e.getMessage())
                        .setId(null)
                        .setMerId(toit.getMerchantId())
                        .setMerOrderId(toit.getMerOrderId())
                        .setPlatformOrderId(toit.getPlatformOrderId())
                        .setAmount(toit.getAmount())
                        .setReturnUrl(null)
                        .setNoticeUrl(null)
                        .setTradeCode(StatusEnum._8.getStatus())
                        .setRequestMsg(StatusEnum._8.getRemark())
                        .setReferPath(" public void transOrderWallet( ActiveMQObjectMessage objectMessage) ")
                        .setResponseResult(JSON.toJSONString(toit))
                        .setTradeTime(new Date())
                        .setCreateTime(new Date()));
            }
        }
    }
}
