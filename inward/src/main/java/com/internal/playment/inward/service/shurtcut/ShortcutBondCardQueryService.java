package com.internal.playment.inward.service.shurtcut;

import com.internal.playment.common.dto.BusinessBondCardQueryDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceInterface;

import java.util.Map;

public interface ShortcutBondCardQueryService extends CommonServiceInterface {

    Map<String, ParamRule> getParamMapByBusinessBondCardQuery();

    Map<String, Object> query(BusinessBondCardQueryDTO businessBondCardQueryDTO, InnerPrintLogObject ipo);

    String responseMsg(BusinessBondCardQueryDTO businessBondCardQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg);
}
