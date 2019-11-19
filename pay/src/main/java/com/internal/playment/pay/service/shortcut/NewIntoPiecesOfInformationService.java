package com.internal.playment.pay.service.shortcut;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerBankCardBindDTO;
import com.internal.playment.common.dto.MerBasicInfoRegDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.ProductGroupTypeTable;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.pay.service.CommonServiceInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewIntoPiecesOfInformationService  extends CommonServiceInterface {
    /**
     *
      * @return
     */
    Map<String, ParamRule> getParamMapByB1();


    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB2();

    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB3();
    /**
     *  根据商户配置获取所有通道
     * @param list
     * @param ipo
     * @return
     * @throws NewPayException
     */
    List<ChannelInfoTable>   getChannelInfoByMerSetting(List<MerchantSettingTable> list, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *   根据产品类型过滤通道
     * @param list
     * @param productType
     * @param ipo
     * @return
     */
//    Tuple2<List<ProductSettingTable>,Set<ChannelInfoTable>> filtrationChannelInfoByProductType(List<ChannelInfoTable> list, String productType, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取子商户成功进件的所有记录
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> getRegisterCollectOnSuccess(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  过滤出已经成功进件的通道
     * @param channelInfoTableSet
     * @param registerCollectTableList
     * @param ipo
     * @return
     */
    LinkedList<ChannelInfoTable> filtrationChannelInfoBySuccessRegisterCollect(Set<ChannelInfoTable> channelInfoTableSet, List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 获取星级最高的通道，如果相同，取最后一个
     * @param channelInfoTablesList
     * @param ipo
     * @return
     */
    ChannelInfoTable filtrationChannelInfoByLevel(LinkedList<ChannelInfoTable> channelInfoTablesList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取进件附属通道信息
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    ChannelExtraInfoTable getAddCusChannelExtraInfo(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  保存进件信息
     * @param mbirDTO
     * @param channelInfoTable
     */
    Tuple2<RegisterInfoTable,RegisterCollectTable> saveByRegister(MerBasicInfoRegDTO mbirDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 更新进件新
     * @param crossResponseMsgDTO
     * @param registerCollectTable
     * @return
     */
    RegisterCollectTable updateByRegisterCollectTable(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  判断多重订单
     * @return
     */
    boolean multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;
    /**
     * 查看订单是否存在
     * @param platformOrderId
     * @param busiType
     * @param ipo
     * @return
     * @throws NewPayException
     */
    RegisterCollectTable getRegisterCollectTable(String platformOrderId, String busiType, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param rct
     * @param ipo
     */
    void checkRepetitionOperation(RegisterCollectTable rct, String busiType, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  更新b2z值
     * @param registerCollectTable
     * @param mbcbDTO
     * @param ipo
     * @return
     */
    Tuple2<RegisterInfoTable,RegisterCollectTable> saveOnRegisterInfo(RegisterCollectTable registerCollectTable, MerBankCardBindDTO mbcbDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param registerCollectTable
     * @param ipo
     * @return
     */
    RegisterCollectTable saveRegisterCollectTableByB3(RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param productGroupType
     * @param ipo
     * @return
     */
    List<ProductGroupTypeTable> getProductGroupTypeInfo(String productGroupType, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param productGroupTypeTableList
     * @param channelInfoTableList
     * @param ipo
     * @return
     */
    Set<ChannelInfoTable> filtrationChannelInfo(List<ProductGroupTypeTable> productGroupTypeTableList, List<ChannelInfoTable> channelInfoTableList, InnerPrintLogObject ipo) throws NewPayException;
}
