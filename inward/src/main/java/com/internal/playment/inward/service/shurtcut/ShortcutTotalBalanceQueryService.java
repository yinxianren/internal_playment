package com.internal.playment.inward.service.shurtcut;

import com.internal.playment.common.dto.BusinessTotalBalanceQueryDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceInterface;

import java.util.Map;

public interface ShortcutTotalBalanceQueryService extends CommonServiceInterface {

    Map<String, ParamRule> getParamMapByBusinessTotalBalanceQuery();

    Map<String, Object> query(BusinessTotalBalanceQueryDTO businessTotalBalanceQueryDTO, InnerPrintLogObject ipo);

    String responseMsg(BusinessTotalBalanceQueryDTO businessTotalBalanceQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg);
}
