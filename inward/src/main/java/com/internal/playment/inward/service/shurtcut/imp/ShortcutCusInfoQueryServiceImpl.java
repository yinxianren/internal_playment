package com.internal.playment.inward.service.shurtcut.imp;


import com.internal.playment.common.dto.BusinessCusInfoQueryDTO;
import com.internal.playment.common.enums.*;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.inner.PayTreeMap;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceAbstract;
import com.internal.playment.inward.service.shurtcut.ShortcutCusInfoQueryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShortcutCusInfoQueryServiceImpl extends CommonServiceAbstract implements ShortcutCusInfoQueryService {


    @Override
    public Map<String, ParamRule> getParamMapByBusinessOrderQuery() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, Object> query(BusinessCusInfoQueryDTO businessCusInfoQueryDTO, InnerPrintLogObject ipo) {

        List<RegisterCollectTable> registerCollectServiceList = dbCommonRPCComponent.apiRegisterCollectService.getList(
                new RegisterCollectTable()
                        .setMerchantId(businessCusInfoQueryDTO.getMerId())
                        .setTerminalMerId(businessCusInfoQueryDTO.getTerMerId())
                        .setBussType(BusinessTypeEnum.b3.getBusiType()));

        PayTreeMap<String,Object> map = new PayTreeMap<String,Object>()
                .lput("merId",businessCusInfoQueryDTO.getMerId())
                .lput("terMerId",businessCusInfoQueryDTO.getTerMerId());
        //没有记录情况下
        if(isHasNotElement(registerCollectServiceList)) return  map
                .lput("errorCode", ResponseCodeEnum.RXH00059.getCode())
                .lput("errorMsg",  ResponseCodeEnum.RXH00059.getMsg());
        //查询是否有成功记录
        RegisterCollectTable registerCollectTable = registerCollectServiceList.stream()
                .filter(reg -> reg.getStatus().equals(StatusEnum._0.getStatus()))
                .reduce( (_1,_2) ->  _1.getUpdateTime().getTime() >= _2.getUpdateTime().getTime() ?  _1 : _2 ).orElse(null);

        if( !isNull(registerCollectTable))  return map
                .lput("merOrderId",registerCollectTable.getMerOrderId())
                .lput("platformOrderId",registerCollectTable.getPlatformOrderId())
                .lput("status",StatusEnum._0.getStatus())
                .lput("msg",StatusEnum._0.getRemark());
        //查询失败记录
        registerCollectTable = registerCollectServiceList.stream()
                .filter(reg -> reg.getStatus().equals(StatusEnum._1.getStatus()))
                .reduce( (_1,_2) ->  _1.getUpdateTime().getTime() >= _2.getUpdateTime().getTime() ?  _1 : _2 ).orElse(null);

        if( !isNull(registerCollectTable))  return map
                .lput("merOrderId",registerCollectTable.getMerOrderId())
                .lput("platformOrderId",registerCollectTable.getPlatformOrderId())
                .lput("status",StatusEnum._1.getStatus())
                .lput("msg",StatusEnum._1.getRemark());

        //查询未处理记录
        registerCollectTable = registerCollectServiceList.stream()
                .filter(reg -> reg.getStatus().equals(StatusEnum._2.getStatus()))
                .reduce( (_1,_2) ->  _1.getUpdateTime().getTime() >= _2.getUpdateTime().getTime() ?  _1 : _2 ).orElse(null);

        if( !isNull(registerCollectTable))  return map
                .lput("merOrderId",registerCollectTable.getMerOrderId())
                .lput("platformOrderId",registerCollectTable.getPlatformOrderId())
                .lput("status",StatusEnum._2.getStatus())
                .lput("msg",StatusEnum._2.getRemark());

        //查询处理中记录
        registerCollectTable = registerCollectServiceList.stream()
                .filter(reg -> reg.getStatus().equals(StatusEnum._3.getStatus()))
                .reduce( (_1,_2) ->  _1.getUpdateTime().getTime() >= _2.getUpdateTime().getTime() ?  _1 : _2 ).orElse(null);

        if( !isNull(registerCollectTable))  return map
                .lput("merOrderId",registerCollectTable.getMerOrderId())
                .lput("platformOrderId",registerCollectTable.getPlatformOrderId())
                .lput("status",StatusEnum._3.getStatus())
                .lput("msg",StatusEnum._3.getRemark());


        registerCollectTable = registerCollectServiceList.stream()
                .reduce( (_1,_2) ->  _1.getUpdateTime().getTime() >= _2.getUpdateTime().getTime() ?  _1 : _2 ).orElse(null);
        return map
                .lput("merOrderId",registerCollectTable.getMerOrderId())
                .lput("platformOrderId",registerCollectTable.getPlatformOrderId())
                .lput("status",registerCollectTable.getStatus())
                .lput("msg",StatusEnum.remark(registerCollectTable.getStatus()));
    }

    @Override
    public String responseMsg(BusinessCusInfoQueryDTO businessCusInfoQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg) {
        PayTreeMap<String,Object> map = new PayTreeMap<String,Object>()
                .lput("merId", null != businessCusInfoQueryDTO ? businessCusInfoQueryDTO.getMerId() : null )
                .lput("terMerId",null != businessCusInfoQueryDTO ? businessCusInfoQueryDTO.getTerMerId() : null )
                .lput("errorCode", errorCode )
                .lput("errorMsg",  errorMsg );
        return responseMsg(map,merInfoTable);
    }
}
