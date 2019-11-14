package com.internal.playment.inward.service.shurtcut;

import com.internal.playment.common.dto.BusinessOrderQueryDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceInterface;

import java.util.Map;

public interface ShortcutOrderQueryService extends CommonServiceInterface {

    Map<String, ParamRule> getParamMapByBusinessOrderQuery();

    Map<String, Object> query(BusinessOrderQueryDTO businessOrderQueryDTO, InnerPrintLogObject ipo);

    String responseMsg(Map<String, Object> map, MerchantInfoTable merInfoTable);

    String responseMsg(BusinessOrderQueryDTO businessOrderQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg);
}
