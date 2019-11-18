package com.internal.playment.api.db.transaction;


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

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午1:45
 * Description:
 */
public interface ApiPayOrderBusinessTransactionService {

    /**
     *
     * @param pit
     * @param cht
     * @param rqtSet
     */
    void updateByPayOrderCorrelationInfo(PayOrderInfoTable pit, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet);

    /**
     *
     * @param merWalletTuple
     * @param terMerWalletTuple
     * @param chanWalletTuple
     * @param agentMerWalletTuple
     * @param poi
     */
    void updateOrSavePayOrderBussInfo(Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple, Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple, Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple, Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple, PayOrderInfoTable poi);
    /**
     *
     * @param merWalletTuple
     * @param terMerWalletTuple
     * @param chanWalletTuple
     * @param agentMerWalletTuple
     * @param toit
     */
    void updateOrSaveTransOrderBussInfo(Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple, Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple, Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple, Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple, List<PayOrderInfoTable> payOrderInfoTableList,TransOrderInfoTable toit);

    /**
     *
     * @param payOrderInfoTable
     * @param payOrderInfoByHedging
     */
    void updateAndSavePayOderMsg(PayOrderInfoTable payOrderInfoTable,PayOrderInfoTable payOrderInfoByHedging);
}
