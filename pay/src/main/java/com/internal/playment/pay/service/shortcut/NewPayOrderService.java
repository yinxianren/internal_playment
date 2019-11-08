package com.internal.playment.pay.service.shortcut;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerNoAuthPayOrderApplyDTO;
import com.internal.playment.common.dto.MerPayOrderApplyDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.channel.ChannelHistoryTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.RiskQuotaTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.pay.service.CommonServiceInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewPayOrderService extends CommonServiceInterface {
    /**
     * 支付下单申请
     * @return
     */
    Map<String, ParamRule> getParamMapByB7();

    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB8();
    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB9();

    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB10();
    /**
     * 查看是否重复订单
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  获取商户风控信息
     * @param merchantId
     * @param ipo
     * @return
     */
    MerchantQuotaRiskTable getMerchantQuotaRiskByMerId(String merchantId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取成功进件记录
     * @param ipo
     * @param  args  MerchantId  TerminalMerId ProductId
     * @return
     */
    List<RegisterCollectTable> getSuccessRegisterInfo(InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     * 根据进件信息获取邦卡记录
     * @param registerCollectTableList
     * @param ipo
     * @return
     */
    List<MerchantCardTable> getSuccessMerchantCardInfo(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  查询通道使用记录
     * @param ipo
     *  @param  strings  MerchantId  TerminalMerId ProductId
     * @return
     */
    ChannelHistoryTable getChannelHistoryInfo(InnerPrintLogObject ipo, String... strings) throws NewPayException;

    /**
     * 判断该卡是否进行绑卡操作
     * @param merchantCardTableList
     * @param merPayOrderApplyDTO
     * @param ipo
     */
    void checkBondCardByCardNum(List<MerchantCardTable> merchantCardTableList, MerPayOrderApplyDTO merPayOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取风控交易量统计数据
     * @param merInfoTable
     * @param ipo
     * @return
     */
    Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByMer(MerchantInfoTable merInfoTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  单笔风控
     * @param amount
     * @param merchantQuotaRiskTable
     * @param ipo
     */
    void checkSingleAmountRisk(String amount, MerchantQuotaRiskTable merchantQuotaRiskTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  执行平台风控
     * @param merchantQuotaRiskTable
     * @param merRiskQuota
     */
    void executePlatformRisk(String amount, MerchantQuotaRiskTable merchantQuotaRiskTable, Tuple2<RiskQuotaTable, RiskQuotaTable> merRiskQuota, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取通道信息
     * @param channelHistoryTable
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfoByChannelHistory(ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 获取该通道历史统计交易量
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByChannel(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param channelInfoTable
     * @param channelRiskQuota
     * @param ipo
     * @param args
     * @return
     * @throws NewPayException
     */
    ChannelInfoTable executeChannelRisk(ChannelInfoTable channelInfoTable, Tuple2<RiskQuotaTable, RiskQuotaTable> channelRiskQuota, InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     * 获取商户可用支付的所有通道
     * @param merPayOrderApplyDTO
     * @param channelHistoryTable
     * @param ipo
     * @return
     */
    List<ChannelInfoTable> getAllUsableChannel(MerPayOrderApplyDTO merPayOrderApplyDTO, ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取一个可行性的通道
     * @param channelInfoTableList
     * @param ipo
     * @return
     */
    ChannelInfoTable getFeasibleChannel(List<ChannelInfoTable> channelInfoTableList, InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     * 保存订单信息
     * @param merPayOrderApplyDTO
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    PayOrderInfoTable savePayOrderInfo(MerchantInfoTable merInfoTable, MerPayOrderApplyDTO merPayOrderApplyDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  更新订单结果
     * @param crossResponseMsgDTO
     * @param crossResponseMsg
     * @param payOrderInfoTable
     */
    PayOrderInfoTable updateByPayOrderInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  生成历史通道信息
     * @param channelHistoryTable
     * @param payOrderInfoTable
     * @return
     */
    ChannelHistoryTable updateByChannelHistoryInfo(ChannelHistoryTable channelHistoryTable, PayOrderInfoTable payOrderInfoTable);

    /**
     *  商户和通道使用汇总情况
     * @param payOrderInfoTable
     * @param merRiskQuota
     * @param channelRiskQuota
     * @return
     */
    Set<RiskQuotaTable> updateByRiskQuotaInfo(PayOrderInfoTable payOrderInfoTable, Tuple2<RiskQuotaTable, RiskQuotaTable> merRiskQuota, Tuple2<RiskQuotaTable, RiskQuotaTable> channelRiskQuota);

    /**
     *
     * @param payOrderInfoTable
     * @param cht
     * @param rqtSet
     * @param ipo
     */
    void batchUpdatePayOderCorrelationInfo(PayOrderInfoTable payOrderInfoTable, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param platformOrderId
     * @param ipo
     * @return
     */
    PayOrderInfoTable getPayOrderInfoByPlatformOrderId(String platformOrderId, String bussType, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merchantCardTableList
     * @param ipo
     * @param args
     * @return
     * @throws NewPayException
     */
    List<MerchantCardTable> filterMerCardByPaymentMsg(List<MerchantCardTable> merchantCardTableList, InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     *
     * @param registerCollectTableList
     * @param merchantCardTableList
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> filterRegCollectInfoByMerCard(List<RegisterCollectTable> registerCollectTableList, List<MerchantCardTable> merchantCardTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param registerCollectTableList
     * @param ipo
     * @param args
     * @return
     * @throws NewPayException
     */
    List<ChannelInfoTable> getAllUsableChannelList(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo, String... args) throws NewPayException ;

    /**
     *
     * @param channelInfoTable
     * @param registerCollectTableList
     * @param ipo
     * @return
     */
    RegisterCollectTable finallyFilterRegCollect(ChannelInfoTable channelInfoTable, List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merchantCardTableList
     * @param ipo
     * @param args
     * @return
     * @throws NewPayException
     */
    MerchantCardTable finallyFilterMerCard(List<MerchantCardTable> merchantCardTableList, InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     *
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    RegisterCollectTable getSuccessRegInfoByChanInfo(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param channelInfoTable
     * @param registerCollectTable
     * @param ipo
     * @return
     */
    MerchantCardTable getMerCardByChanAndReg(ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     *
     * @param registerCollectTableList
     * @param merchantSettingTableList
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> filterRegCollectByMerSet(List<RegisterCollectTable> registerCollectTableList, List<MerchantSettingTable> merchantSettingTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param channelInfoTableList
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    List<ChannelInfoTable> subtractUnableChanInfo(List<ChannelInfoTable> channelInfoTableList, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param channelInfoTable
     * @param merchantSettingTableList
     * @return
     */
    ChannelInfoTable judgeThisChannelUsable(ChannelInfoTable channelInfoTable, List<MerchantSettingTable> merchantSettingTableList);

    /**
     *
     * @param merInfoTable
     * @param merPayOrderApplyDTO
     * @param channelInfoTable
     * @param registerCollectTable
     * @param merchantCardTable
     * @param ipo
     * @return
     */
    PayOrderInfoTable savePayOrder(MerchantInfoTable merInfoTable, MerPayOrderApplyDTO merPayOrderApplyDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException;


    /**
     *
     * @param payOrderInfoTable
     * @param ipo
     * @param args
     * @return
     * @throws NewPayException
     */
    PayOrderInfoTable updateByPayOrderInfoByBefore(PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo, String... args) throws NewPayException;

    /**
     *
     * @param payOrderInfoTable
     * @return
     */
    MerPayOrderApplyDTO getMerPayOrderApplyDTO(PayOrderInfoTable payOrderInfoTable);

    /**
     *
     * @param payOrderInfoTable
     * @param ipo
     */
    void checkPayOrderInfoTableByB9(PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param crossResponseMsgDTO
     * @param crossResponseMsg
     * @param payOrderInfoTable
     * @param ipo
     * @return
     */
    PayOrderInfoTable updateByPayOrderInfoByB9After(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merInfoTable
     * @param merNoAuthPayOrderApplyDTO
     * @param channelInfoTable
     * @param registerCollectTable
     * @param merchantCardTable
     * @param ipo
     * @return
     */
    PayOrderInfoTable savePayOrderByNoAuth(MerchantInfoTable merInfoTable, MerNoAuthPayOrderApplyDTO merNoAuthPayOrderApplyDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException;

}
