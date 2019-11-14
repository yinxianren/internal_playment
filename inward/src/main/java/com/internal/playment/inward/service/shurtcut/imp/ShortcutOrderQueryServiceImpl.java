package com.internal.playment.inward.service.shurtcut.imp;

import com.alibaba.fastjson.JSON;
import com.internal.playment.common.dto.BusinessOrderQueryDTO;
import com.internal.playment.common.enums.*;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.inner.PayTreeMap;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.inward.service.CommonServiceAbstract;
import com.internal.playment.inward.service.shurtcut.ShortcutOrderQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShortcutOrderQueryServiceImpl extends CommonServiceAbstract implements ShortcutOrderQueryService {


    @Override
    public Map<String, ParamRule> getParamMapByBusinessOrderQuery() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));// 商户订单号
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, Object> query(BusinessOrderQueryDTO businessOrderQueryDTO, InnerPrintLogObject ipo) {

        List<String>  pay = Arrays.asList(
                ProductTypeEnum.RH_QUICKPAY_SMALL.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_SMALL_NOSMS.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_SMALL_REP.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_SMALL_NOSMS_REP.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_LARGE_REP.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId().toUpperCase());
        List<String>  trans = Arrays.asList(ProductTypeEnum.RH_TRX_PAY.getProductId().toUpperCase());
        String productType = businessOrderQueryDTO.getProductType().trim().toUpperCase();
        PayTreeMap<String, Object> map = new PayTreeMap<String, Object>()
                .lput("merId",businessOrderQueryDTO.getMerId())
                .lput("terMerId",businessOrderQueryDTO.getTerMerId())
                .lput("merOrderId",businessOrderQueryDTO.getMerOrderId())
                ;
        //*******【收单查询】**********************************************************************************************************
        if(pay.contains(productType)){
            //查询订单
            PayOrderInfoTable payOrderInfoTable = dbCommonRPCComponent.apiPayOrderInfoService.getOne(new PayOrderInfoTable()
                    .setTerminalMerId(businessOrderQueryDTO.getTerMerId())
                    .setMerchantId(businessOrderQueryDTO.getMerId())
                    .setMerOrderId(businessOrderQueryDTO.getMerOrderId())
            );
            //判断订单是否存在
            if(isNull(payOrderInfoTable))  return map
                    .lput("errorCode", ResponseCodeEnum.RXH00058.getCode())
                    .lput("errorMsg",ResponseCodeEnum.RXH00058.getMsg());

            //判断订单状态
            int status = payOrderInfoTable.getStatus();
            switch (status){
                case 0 :
                    map.lput("status",StatusEnum._0.getStatus()).lput("msg",StatusEnum._0.getRemark());
                    break;
                case 1 :
                    map.lput("status",StatusEnum._1.getStatus()).lput("msg",StatusEnum._1.getRemark());
                    break;
                case 2 :
                    map.lput("status",StatusEnum._2.getStatus()).lput("msg",StatusEnum._2.getRemark());
                    break;
                case 3 :
                    map.lput("status",StatusEnum._3.getStatus()).lput("msg",StatusEnum._3.getRemark());
                    break;
                case 7 :
                case 8 :
                case 9 :
                    map.lput("status",StatusEnum._0.getStatus()).lput("msg","该订单处于代付处理状态");
                    break;
                case 10 :
                    map.lput("status",StatusEnum._0.getStatus()).lput("msg","该订单已经完成全额代付");
                    break;
                case 11 :
                    {
                    List<TransOrderInfoTable> transOrderInfoTableList = dbCommonRPCComponent.apiTransOrderInfoService.getList(new TransOrderInfoTable()
                            .setOrgMerOrderId(payOrderInfoTable.getMerOrderId())
                            .setMerchantId(payOrderInfoTable.getMerchantId())
                            .setTerminalMerId(payOrderInfoTable.getTerminalMerId())
                            .setStatus(StatusEnum._0.getStatus()));
                    //已经代付过的金额
                    BigDecimal transOrderAmount = transOrderInfoTableList.stream()
                            .map(TransOrderInfoTable::getAmount)
                            .reduce((_1, _2) -> _1.add(_2)).get();
                    //收单入账金额
                    BigDecimal payOrderAmount = payOrderInfoTable.getInAmount();
                    //剩余未代付金额
                    BigDecimal residueAmount = payOrderAmount.subtract(transOrderAmount);
                    map
                            .lput("status", StatusEnum._0.getStatus())
                            .lput("msg", format("该订单已经完成部分代付，已代付金额：%s,未代付金额：%s",transOrderAmount,residueAmount));
                 }
                break;
            }

            return map
                    .lput("platformOrderId",payOrderInfoTable.getPlatformOrderId())
                    .lput("amount",payOrderInfoTable.getAmount());
        }
        //*******【代付查询】**********************************************************************************************************
        if(trans.contains(productType)){
            TransOrderInfoTable transOrderInfoTable = dbCommonRPCComponent.apiTransOrderInfoService.getOne( new TransOrderInfoTable()
                    .setTerminalMerId(businessOrderQueryDTO.getTerMerId())
                    .setMerchantId(businessOrderQueryDTO.getMerId())
                    .setMerOrderId(businessOrderQueryDTO.getMerOrderId())
            );
            if(isNull(transOrderInfoTable))  return map
                    .lput("errorCode", ResponseCodeEnum.RXH00058.getCode())
                    .lput("errorMsg",ResponseCodeEnum.RXH00058.getMsg());
             int status = transOrderInfoTable.getStatus();
            switch (status){
                case 0 :
                    map.lput("status",StatusEnum._0.getStatus()).lput("msg",StatusEnum._0.getRemark());
                    break;
                case 1 :
                    map.lput("status",StatusEnum._1.getStatus()).lput("msg",StatusEnum._1.getRemark());
                    break;
                case 2 :
                    map.lput("status",StatusEnum._2.getStatus()).lput("msg",StatusEnum._2.getRemark());
                    break;
                case 3 :
                    map.lput("status",StatusEnum._3.getStatus()).lput("msg",StatusEnum._3.getRemark());
                    break;
                case 7 :
                case 8 :
                case 9 :
                    map.lput("status",StatusEnum._0.getStatus()).lput("msg","该订单处于代付处理状态");
                    break;
            }
            return map
                    .lput("platformOrderId",transOrderInfoTable.getPlatformOrderId())
                    .lput("amount",transOrderInfoTable.getAmount());
        }
        //*******【产品类型错误】**********************************************************************************************************
        return   map.lput("errorCode", ResponseCodeEnum.RXH00021.getCode())
                .lput("errorMsg",ResponseCodeEnum.RXH00021.getMsg());
    }

    @Override
    public String responseMsg(Map<String, Object> map, MerchantInfoTable merInfoTable) {
        String signMsg = md5Component.getMd5SignWithKey(map,merInfoTable.getSecretKey());
        map.put("signMsg",signMsg);
        return JSON.toJSONString(map);
    }

    @Override
    public String responseMsg(BusinessOrderQueryDTO businessOrderQueryDTO, MerchantInfoTable merInfoTable, String errorCode, String errorMsg) {
        PayTreeMap<String, Object> map = new PayTreeMap<String, Object>()
                .lput("merId",null != businessOrderQueryDTO ? businessOrderQueryDTO.getMerId() : null )
                .lput("terMerId",null != businessOrderQueryDTO ? businessOrderQueryDTO.getTerMerId() : null )
                .lput("merOrderId",null != businessOrderQueryDTO ? businessOrderQueryDTO.getMerOrderId(): null )
                .lput("errorCode", errorCode )
                .lput("errorMsg", errorMsg );
        return responseMsg(map,merInfoTable);
    }

/*
platformOrderId	平台订单号
amount	订单金额
status	订单状态	订单状态（0成功、1失败、2未处理、3处理中）
msg	结果描述
signMsg	签名字符串
errorCode	错误码	非成功时才返回，参照3.4异常码
errorMsg	错误信息
 */











    /*
        signMsg	签名字符串		是	256	成功必有值
*/
}

