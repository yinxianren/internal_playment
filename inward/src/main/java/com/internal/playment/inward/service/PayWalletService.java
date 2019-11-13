package com.internal.playment.inward.service;


import com.internal.playment.common.inner.InnerPrintLogObject;
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
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.common.tuple.Tuple2;

import java.util.List;

public interface PayWalletService {
    /**
     *  获取商户信息
     * @param ipo
     * @return
     */
    MerchantInfoTable getMerInfo(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param ipo
     * @param args
     * @return
     * @throws NewPayException
     */
    MerchantRateTable getMerRate(InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     *
     * @param ipo
     * @return
     */
    MerchantWalletTable getMerWallet(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param mwt
     * @param poi
     * @param mrt
     * @return
     */
    Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWalletByPayOrder(MerchantWalletTable mwt, PayOrderInfoTable poi, MerchantRateTable mrt);

    /**
     *
     * @param ipo
     * @return
     */
    TerminalMerchantsWalletTable getTerMerWallet(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param tmw
     * @param poi
     * @return
     */
    Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWalletByPayOrder(TerminalMerchantsWalletTable tmw, PayOrderInfoTable poi, MerchantRateTable mrt);

    /**
     *
     * @param channelId
     * @param ipo
     * @return
     */
    ChannelWalletTable getChanWallet(String channelId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param channelId
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfo(String channelId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param cwt
     * @param cit
     * @param poi
     * @return
     */
    Tuple2<ChannelWalletTable, ChannelDetailsTable> updateChannelWalletByPayOrder(ChannelWalletTable cwt, ChannelInfoTable cit, PayOrderInfoTable poi, MerchantRateTable mrt);

    /**
     *
     * @param agentMerchantId
     * @param ipo
     * @return
     */
    AgentMerchantSettingTable getAgentMerSet(String agentMerchantId, String productId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param agentMerchantId
     * @param ipo
     * @return
     */
    AgentMerchantWalletTable getAgentMerWallet(String agentMerchantId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param amw
     * @param ams
     * @param poi
     * @return
     */
    Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> updateAgentMerWalletByPayOrder(AgentMerchantWalletTable amw, AgentMerchantSettingTable ams, PayOrderInfoTable poi);

    /**
     *
     * @param mwt
     * @param toit
     * @param mrt
     * @return
     */
    Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWalletByTransOrder(MerchantWalletTable mwt, TransOrderInfoTable toit, MerchantRateTable mrt) throws Exception;

    /**
     *
     * @param tmw
     * @param toit
     * @param mrt
     * @return
     */
    Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWalletByTransOrder(TerminalMerchantsWalletTable tmw, TransOrderInfoTable toit, MerchantRateTable mrt) throws Exception;

    /**
     *
     * @param cwt
     * @param cit
     * @param toit
     * @param mrt
     * @return
     */
    Tuple2<ChannelWalletTable, ChannelDetailsTable> updateChannelWalletByTransOrder(ChannelWalletTable cwt, ChannelInfoTable cit, TransOrderInfoTable toit, MerchantRateTable mrt) throws Exception;

    /**
     *
     * @param amw
     * @param ams
     * @param toit
     * @return
     */
    Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> updateAgentMerWalletByTransOrder(AgentMerchantWalletTable amw, AgentMerchantSettingTable ams, TransOrderInfoTable toit);

    /**
     *
     * @param poi
     * @param ipo
     */
    void checkPayOrderOperability(PayOrderInfoTable poi, InnerPrintLogObject ipo) throws Exception;

    /**
     *
     * @param toit
     * @param ipo
     */
    void checkTransOrderOperability(TransOrderInfoTable toit, InnerPrintLogObject ipo) throws Exception;

    /**
     *
     * @param toit
     * @param ipo
     * @return
     */
    List<PayOrderInfoTable> getPayOrderInfo(TransOrderInfoTable toit, InnerPrintLogObject ipo) throws Exception;
}
