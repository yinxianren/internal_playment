package com.internal.playment.inward.service.shurtcut.imp;

import com.internal.playment.common.dto.BusinessBondCardQueryDTO;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.inner.PayMap;
import com.internal.playment.common.inner.PayTreeMap;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceAbstract;
import com.internal.playment.inward.service.shurtcut.ShortcutBondCardQueryService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShortcutBondCardQueryServiceImpl extends CommonServiceAbstract implements ShortcutBondCardQueryService {


    @Override
    public Map<String, ParamRule> getParamMapByBusinessBondCardQuery() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
//                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//银行卡号
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, Object> query(BusinessBondCardQueryDTO businessBondCardQueryDTO, InnerPrintLogObject ipo) {
        PayTreeMap<String,Object>  map = new PayTreeMap<String, Object>()
                .lput("merId",businessBondCardQueryDTO.getMerId())
                .lput("terMerId",businessBondCardQueryDTO.getTerMerId());

        List<MerchantCardTable> merchantCardTableList = dbCommonRPCComponent.apiMerchantCardService.getList(
                new MerchantCardTable()
                        .setMerchantId(businessBondCardQueryDTO.getMerId())
                        .setTerminalMerId(businessBondCardQueryDTO.getTerMerId())
                        .setBankCardNum(businessBondCardQueryDTO.getBankCardNum())
                        .setBussType(BusinessTypeEnum.b6.getBusiType())
        );

        if(isHasNotElement(merchantCardTableList)) return map
                .lput("errorCode", ResponseCodeEnum.RXH00060.getCode())
                .lput("errorMsg", ResponseCodeEnum.RXH00060.getMsg());
        Set<Map<String,Object>> list = new HashSet<>();
        merchantCardTableList.forEach( _1 ->list.add(
                new PayMap<String,Object>()
                        .lput("bankCardNum",_1.getBankCardNum())
                        .lput("status",_1.getStatus())
                        .lput("channelTab", _1.getOrganizationId())
                )
        );
        return map
                .lput("bondCardList",list)
                .lput("status",StatusEnum._0.getStatus())
                .lput("msg",StatusEnum._0.getRemark());
    }

    @Override
    public String responseMsg(BusinessBondCardQueryDTO businessBondCardQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg) {
        PayTreeMap<String,Object> map = new PayTreeMap<String,Object>()
                .lput("merId", null != businessBondCardQueryDTO ? businessBondCardQueryDTO.getMerId() : null )
                .lput("terMerId", null != businessBondCardQueryDTO ? businessBondCardQueryDTO.getTerMerId() : null )
                .lput("status",StatusEnum._1.getStatus())
                .lput("msg",StatusEnum._1.getRemark())
                .lput("errorCode",errorCode)
                .lput("errorMsg",errorMsg);
        return responseMsg(map,merInfoTable);
    }
}
