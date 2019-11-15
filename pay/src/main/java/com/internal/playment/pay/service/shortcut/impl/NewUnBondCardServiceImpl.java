package com.internal.playment.pay.service.shortcut.impl;

import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.pay.service.CommonServiceAbstract;
import com.internal.playment.pay.service.shortcut.NewUnBondCardService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NewUnBondCardServiceImpl extends CommonServiceAbstract implements NewUnBondCardService {

    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) throws NewPayException {
        return null;
    }


    @Override
    public Map<String, ParamRule> getParamMapByUnBondCard() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//银行卡号
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//证件号码
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }
}
