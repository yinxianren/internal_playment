package com.internal.playment.inward.service;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;

import java.util.List;
import java.util.Map;

public interface CommonServiceInterface {

    List<MerchantSettingTable> getMerchantSetting(InnerPrintLogObject ipo) throws NewPayException;

    MerchantInfoTable getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException;

    String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    boolean saveSysLog(SystemOrderTrackTable systemOrderTrackTable);

    ChannelInfoTable getChannelInfoByChannelId(String channelId, InnerPrintLogObject ipo) throws NewPayException;

    ChannelExtraInfoTable getChannelExtraInfoByOrgId(String organizationId, String bussType, InnerPrintLogObject ipo) throws NewPayException;

    String responseMsg(MerchantInfoTable merInfoTable, InnerPrintLogObject ipo, CrossResponseMsgDTO crossResponseMsgDTO, String ...args) throws NewPayException, IllegalAccessException;

    String responseMsg(Map<String, Object> map, MerchantInfoTable merInfoTable);
}
