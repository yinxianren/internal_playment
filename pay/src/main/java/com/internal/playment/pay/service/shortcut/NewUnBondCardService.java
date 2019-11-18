package com.internal.playment.pay.service.shortcut;

import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.pay.service.CommonServiceInterface;

import java.util.Map;

public interface NewUnBondCardService extends CommonServiceInterface {

    Map<String, ParamRule> getParamMapByUnBondCard();

    OrganizationInfoTable getOrganizationInfoByUnBondCard(String channelTab, InnerPrintLogObject ipo) throws NewPayException;

    void update(MerchantCardTable setUpdateTime, InnerPrintLogObject ipo) throws NewPayException;
}
