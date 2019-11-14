package com.internal.playment.inward.service.shurtcut;

import com.internal.playment.common.dto.BusinessCusInfoQueryDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceInterface;

import java.util.Map;

public interface ShortcutCusInfoQueryService extends CommonServiceInterface {

    Map<String, ParamRule> getParamMapByBusinessOrderQuery();

    Map<String, Object> query(BusinessCusInfoQueryDTO businessCusInfoQueryDTO, InnerPrintLogObject ipo);

    String responseMsg(BusinessCusInfoQueryDTO businessCusInfoQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg);
}
