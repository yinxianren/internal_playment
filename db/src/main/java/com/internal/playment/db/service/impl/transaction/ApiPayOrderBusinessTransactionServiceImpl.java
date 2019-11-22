package com.internal.playment.db.service.impl.transaction;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.internal.playment.api.db.transaction.ApiPayOrderBusinessTransactionService;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelDetailsTable;
import com.internal.playment.common.table.channel.ChannelHistoryTable;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;
import com.internal.playment.common.table.system.RiskQuotaTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.db.service.db.agent.AgentMerchantWalletDBService;
import com.internal.playment.db.service.db.agent.AgentMerchantsDetailsDBService;
import com.internal.playment.db.service.db.business.PayOrderInfoDBService;
import com.internal.playment.db.service.db.business.TransOrderInfoDBService;
import com.internal.playment.db.service.db.channel.ChannelDetailsDbService;
import com.internal.playment.db.service.db.channel.ChannelHistoryDbService;
import com.internal.playment.db.service.db.channel.ChannelWalletDbService;
import com.internal.playment.db.service.db.merchant.MerchantWalletDBService;
import com.internal.playment.db.service.db.merchant.MerchantsDetailsDBService;
import com.internal.playment.db.service.db.system.RiskQuotaDBService;
import com.internal.playment.db.service.db.terminal.TerminalMerchantsDetailsDBService;
import com.internal.playment.db.service.db.terminal.TerminalMerchantsWalletDBservice;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午1:50
 * Description:
 */

@Transactional(rollbackFor= Exception.class)
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiPayOrderBusinessTransactionServiceImpl implements ApiPayOrderBusinessTransactionService {

    private final PayOrderInfoDBService payOrderInfoDBService;
    private final TransOrderInfoDBService transOrderInfoDBService;
    private final ChannelHistoryDbService channelHistoryDbService;
    private final RiskQuotaDBService riskQuotaDBService;

    private final MerchantWalletDBService merchantWalletDBService;
    private final MerchantsDetailsDBService merchantsDetailsDBService;

    private final TerminalMerchantsWalletDBservice terminalMerchantsWalletDBservice;
    private final TerminalMerchantsDetailsDBService terminalMerchantsDetailsDBService;

    private final ChannelWalletDbService channelWalletDbService;
    private final ChannelDetailsDbService channelDetailsDbService;

    private final AgentMerchantWalletDBService agentMerchantWalletDBService;
    private final AgentMerchantsDetailsDBService agentMerchantsDetailsDBService;

    @Override
    public void updateByPayOrderCorrelationInfo(PayOrderInfoTable pit, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet) {
        channelHistoryDbService.saveOrUpdate(cht);
        riskQuotaDBService.saveOrUpdateBatch(rqtSet);
        payOrderInfoDBService.update(pit,
                new UpdateWrapper<PayOrderInfoTable>().lambda().eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId()));
    }

    @Override
    public void updateOrSavePayOrderBussInfo(
            Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple,
            Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple,
            Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple,
            Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple,
            PayOrderInfoTable poi) {
        //商户
        merchantWalletDBService.saveOrUpdate(merWalletTuple._1);
        merchantsDetailsDBService.save(merWalletTuple._2);
        //终端商户
        terminalMerchantsWalletDBservice.saveOrUpdate(terMerWalletTuple._1);
        terminalMerchantsDetailsDBService.save(terMerWalletTuple._2);
        //通道
        channelWalletDbService.saveOrUpdate(chanWalletTuple._1);
        channelDetailsDbService.save(chanWalletTuple._2);
        //终端商户
        agentMerchantWalletDBService.saveOrUpdate(agentMerWalletTuple._1);
        agentMerchantsDetailsDBService.save(agentMerWalletTuple._2);
        //更新订单
        payOrderInfoDBService.updateById(poi);
    }

    @Override
    public void updateOrSaveTransOrderBussInfo(Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple,
                                               Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple,
                                               Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple,
                                               Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple,
                                               List<PayOrderInfoTable> payOrderInfoTableList,
                                               TransOrderInfoTable toit) {
        //商户
        merchantWalletDBService.updateById(merWalletTuple._1);
        merchantsDetailsDBService.save(merWalletTuple._2);
        //终端商户
        terminalMerchantsWalletDBservice.updateById(terMerWalletTuple._1);
        terminalMerchantsDetailsDBService.save(terMerWalletTuple._2);
        //通道
        channelWalletDbService.updateById(chanWalletTuple._1);
        channelDetailsDbService.save(chanWalletTuple._2);
        //终端商户
        agentMerchantWalletDBService.updateById(agentMerWalletTuple._1);
        agentMerchantsDetailsDBService.save(agentMerWalletTuple._2);
        //更新收单
        payOrderInfoDBService.updateBatchById(payOrderInfoTableList);
        //更新订单
        transOrderInfoDBService.updateById(toit);
    }

    @Override
    public void updateAndSavePayOderMsg(PayOrderInfoTable payOrderInfoTable, PayOrderInfoTable payOrderInfoByHedging) {
        payOrderInfoDBService.updateById(payOrderInfoTable);
        payOrderInfoDBService.save(payOrderInfoByHedging);
    }

}
