package com.internal.playment.inward.service.shurtcut.imp;

import com.internal.playment.common.dto.BusinessTotalBalanceQueryDTO;
import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.inner.PayTreeMap;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.inward.service.CommonServiceAbstract;
import com.internal.playment.inward.service.shurtcut.ShortcutTotalBalanceQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class ShortcutTotalBalanceQueryServiceImpl extends CommonServiceAbstract implements ShortcutTotalBalanceQueryService {

    @Override
    public Map<String, ParamRule> getParamMapByBusinessTotalBalanceQuery() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, Object> query(BusinessTotalBalanceQueryDTO businessTotalBalanceQueryDTO, InnerPrintLogObject ipo) {
        PayTreeMap<String,Object> map = new PayTreeMap<String,Object>() .lput("merId",businessTotalBalanceQueryDTO.getMerId());
        String terMerId =  businessTotalBalanceQueryDTO.getTerMerId();
        if(isNull(terMerId)){
            MerchantWalletTable merchantWalletTable = dbCommonRPCComponent.apiMerchantWalletService.getOne(
                    new MerchantWalletTable().setMerchantId(businessTotalBalanceQueryDTO.getMerId()));
            return map
                    .lput("balance", null == merchantWalletTable ? new BigDecimal(0) : merchantWalletTable.getTotalBalance())
                    .lput("status", StatusEnum._0.getStatus())
                    .lput("msg", StatusEnum._0.getRemark());
        }
        TerminalMerchantsWalletTable terminalMerchantsWalletTable = dbCommonRPCComponent.apiTerminalMerchantsWalletService.getOne(
                new TerminalMerchantsWalletTable()
                        .setMerchantId(businessTotalBalanceQueryDTO.getMerId())
                        .setTerminalMerId(businessTotalBalanceQueryDTO.getTerMerId()));
        return map
                .lput("balance", null == terminalMerchantsWalletTable ? new BigDecimal(0) : terminalMerchantsWalletTable.getTotalBalance())
                .lput("status", StatusEnum._0.getStatus())
                .lput("msg", StatusEnum._0.getRemark());
    }

    @Override
    public String responseMsg(BusinessTotalBalanceQueryDTO businessTotalBalanceQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg) {
        PayTreeMap<String,Object> map = new PayTreeMap<String,Object>()
                .lput("merId",null != businessTotalBalanceQueryDTO ? businessTotalBalanceQueryDTO.getMerId() : null )
                .lput("terMerId",null != businessTotalBalanceQueryDTO ? businessTotalBalanceQueryDTO.getTerMerId() : null )
                .lput("status",StatusEnum._1.getStatus())
                .lput("signMsg",StatusEnum._1.getRemark())
                .lput("errorCode",errorCode)
                .lput("errorMsg",errorMsg);
        return  responseMsg(map,merInfoTable);
    }
}
