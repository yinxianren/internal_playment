package com.internal.playment.pay.service;


import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.tuple.Tuple2;

import java.util.List;
import java.util.Map;

public interface CommonServiceInterface {
    /**
     *  获取商户信息
     * @param ipo
     * @return
     * @throws NewPayException
     */
    MerchantInfoTable getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException;


    /**
     *
     * @param organizationId
     * @param ipo
     * @return
     */
    OrganizationInfoTable getOrganizationInfo(String organizationId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取商户所有通道配置信息
     * @param ipo
     * @return
     */
    List<MerchantSettingTable> getMerchantSetting(InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 封装请求cross必要参数
     * @param tuple
     * @return
     */
    RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple)throws NewPayException;

    /**
     *
     * @param merInfoTable
     * @param ipo
     * @param crossResponseMsgDTO
     * @param args
     * @return
     * @throws NewPayException
     * @throws IllegalAccessException
     */
    String responseMsg(MerchantInfoTable merInfoTable, InnerPrintLogObject ipo, CrossResponseMsgDTO crossResponseMsgDTO, String... args) throws NewPayException, IllegalAccessException ;

    /**
     *   请求cross
     * @param requestCrossMsgDTO
     * @param extraInfoTable
     * @param ipo
     * @return
     */
    String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo)throws NewPayException;

    /**
     *   请求cross
     * @param requestCrossMsgDTO
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo)throws NewPayException;
    /**
     *  将结果转对象
     * @param crossResponseMsg
     * @param ipo
     * @return
     */
    CrossResponseMsgDTO jsonToPojo(String crossResponseMsg, InnerPrintLogObject ipo)throws NewPayException;

    /**
     *
     * @param systemOrderTrackTable
     * @return
     */
    boolean saveSysLog(SystemOrderTrackTable systemOrderTrackTable);

    /**
     *  获取通道信息
     * @param channelId
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfoByChannelId(String channelId, InnerPrintLogObject ipo)throws NewPayException;

    /**
     *  获取附属通道信息
     * @param organizationId
     * @param bussType
     * @param ipo
     * @return
     */
    ChannelExtraInfoTable getChannelExtraInfoByOrgId(String organizationId, String bussType, InnerPrintLogObject ipo)throws NewPayException;

    /**
     *  获取进件主表
     * @param ritId
     * @param ipo
     * @return
     */
    RegisterInfoTable getRegisterInfoTable(Long ritId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 更加平台订单号获取B4操作记录
     * @param platformOrderId
     * @param ipo
     * @return
     */
    MerchantCardTable getMerchantCardInfoByPlatformOrderId(String platformOrderId, String busiType, InnerPrintLogObject ipo) throws NewPayException;

    MerchantCardTable getMerchantCardInfo( MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *
     * @param regPlatformOrderId
     * @param busiType
     * @param ipo
     * @return
     */
    RegisterCollectTable getRegCollectInfo(String regPlatformOrderId, String busiType, InnerPrintLogObject ipo) throws NewPayException;

    RegisterCollectTable getRegCollectInfo(RegisterCollectTable registerCollectTable,InnerPrintLogObject ipo)throws NewPayException;
    /**
     *
     * @param crossResponseMsgDTO
     * @param ipo
     */
    void isSuccess(CrossResponseMsgDTO crossResponseMsgDTO, InnerPrintLogObject ipo)throws NewPayException;
}
