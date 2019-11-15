package com.internal.playment.pay.service.shortcut;

import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.pay.service.CommonServiceInterface;

import java.util.Map;

public interface NewUnBondCardService extends CommonServiceInterface {

    Map<String, ParamRule> getParamMapByUnBondCard();

}
