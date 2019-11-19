package com.internal.playment.pay.service.shortcut.impl;

import com.internal.playment.common.dto.*;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple4;
import com.internal.playment.pay.service.CommonServiceAbstract;
import com.internal.playment.pay.service.shortcut.NewBondCardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午9:52
 * Description:
 */
@Service
public class NewBondCardServiceImpl extends CommonServiceAbstract implements NewBondCardService {


    @Override
    public void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="multipleOrder";
        List<MerchantCardTable> merchantCardTableList =null;
        try {
            merchantCardTableList = dbCommonRPCComponent.apiMerchantCardService.getList(new MerchantCardTable()
                    .setMerOrderId(merOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
            );
        }catch (Exception e){
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询订单是否重复，发生异常！异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasElement(merchantCardTableList,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));

    }

    @Override
    public MerchantCardTable saveCardInfoByB4(MerBondCardApplyDTO mbcaDTO, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveCardInfoByB4";
        MerchantCardTable  merchantCardTable = new MerchantCardTable()
                .setRegisterCollectPlatformOrderId(registerCollectTable.getPlatformOrderId())
                .setOrganizationId(registerCollectTable.getOrganizationId())
                .setMerchantId(mbcaDTO.getMerId())
                .setMerOrderId(mbcaDTO.getMerOrderId())
                .setTerminalMerId(mbcaDTO.getTerMerId())
                .setCardHolderName(mbcaDTO.getCardHolderName())
                .setIdentityType(Integer.valueOf(mbcaDTO.getIdentityType()))
                .setIdentityNum(mbcaDTO.getIdentityNum())
                .setBankCode(mbcaDTO.getBankCode())
                .setBankName(mbcaDTO.getBankName())
                .setBankAccountProp(Integer.valueOf(mbcaDTO.getBankAccountProp()))
                .setSubBankName(mbcaDTO.getSubBankName())
                .setBankCardType(Integer.valueOf(mbcaDTO.getBankCardType()))
                .setBankCardNum(mbcaDTO.getBankCardNum())
                .setBankCardPhone(mbcaDTO.getBankCardPhone())
                .setValidDate(mbcaDTO.getValidDate())
                .setSecurityCode(mbcaDTO.getSecurityCode())
                .setChannelRespResult(null).setCrossRespResult(null)
                .setBussType(BusinessTypeEnum.b4.getBusiType())
                .setStatus(StatusEnum._3.getStatus()).setCreateTime(new Date()).setUpdateTime(new Date());
        try {
            synchronized (this){
                merchantCardTable
                        .setId(System.currentTimeMillis())
                        .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B4" + System.currentTimeMillis());
            }
            dbCommonRPCComponent.apiMerchantCardService.save(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：B4保存绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return merchantCardTable;
    }

    @Override
    public MerchantCardTable saveCardInfoByB5(MerchantCardTable merchantCardTable, MerReGetBondCodeDTO mrgbcDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveCardInfoByB5";
        merchantCardTable
                .setTerminalMerId(mrgbcDTO.getTerMerId())
                .setCardHolderName(mrgbcDTO.getCardHolderName())
                .setIdentityType(Integer.valueOf(mrgbcDTO.getIdentityType()))
                .setIdentityNum(mrgbcDTO.getIdentityNum())
                .setBankCode(mrgbcDTO.getBankCode())
                .setBankCardType(Integer.valueOf(mrgbcDTO.getBankCardType()))
                .setBankCardNum(mrgbcDTO.getBankCardNum())
                .setBankCardPhone(mrgbcDTO.getBankCardPhone())
                .setValidDate(mrgbcDTO.getValidDate())
                .setSecurityCode(mrgbcDTO.getSecurityCode())
                .setChannelRespResult(null).setCrossRespResult(null)
                .setBussType(BusinessTypeEnum.b5.getBusiType())
                .setStatus(StatusEnum._3.getStatus()).setCreateTime(new Date()).setUpdateTime(new Date());

        try {
            synchronized (this){
                merchantCardTable
                        .setId(System.currentTimeMillis())
                        .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B5" + System.currentTimeMillis());
            }
            dbCommonRPCComponent.apiMerchantCardService.save(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：B5保存绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return merchantCardTable;
    }

    @Override
    public MerchantCardTable saveCardInfoByB6(MerchantCardTable merchantCardTable, MerConfirmBondCardDTO mcbcDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveCardInfoByB6";
        merchantCardTable
                .setTerminalMerId(mcbcDTO.getTerMerId())
                .setCardHolderName(mcbcDTO.getCardHolderName())
                .setIdentityType(Integer.valueOf(mcbcDTO.getIdentityType()))
                .setIdentityNum(mcbcDTO.getIdentityNum())
                .setBankCode(mcbcDTO.getBankCode())
                .setBankCardType(Integer.valueOf(mcbcDTO.getBankCardType()))
                .setBankCardNum(mcbcDTO.getBankCardNum())
                .setBankCardPhone(mcbcDTO.getBankCardPhone())
                .setValidDate(mcbcDTO.getValidDate())
                .setSecurityCode(mcbcDTO.getSecurityCode())
                .setChannelRespResult(null).setCrossRespResult(null)
                .setBussType(BusinessTypeEnum.b6.getBusiType())
                .setPayFee(new BigDecimal(mcbcDTO.getPayFee()))
                .setBackFee(new BigDecimal(mcbcDTO.getBackFee()))
                .setBackCardNum(mcbcDTO.getBankCardNum())
                .setBackBankCode(mcbcDTO.getBackBankCode())
                .setBackCardPhone(mcbcDTO.getBackCardPhone())
                .setStatus(StatusEnum._3.getStatus()).setCreateTime(new Date()).setUpdateTime(new Date());
        try {
            synchronized (this){
                merchantCardTable
                        .setId(System.currentTimeMillis())
                        .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B5" + System.currentTimeMillis());
            }
            dbCommonRPCComponent.apiMerchantCardService.save(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：B6保存绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return merchantCardTable.setSmsCode(mcbcDTO.getSmsCode());
    }

    @Override
    public void updateByBondCardInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByBondCardInfo";
        merchantCardTable.setCrossRespResult(crossResponseMsg)
                .setChannelRespResult( null == crossResponseMsgDTO ? null  : crossResponseMsgDTO.getChannelResponseMsg() )
                .setStatus( null == crossResponseMsgDTO ? StatusEnum._1.getStatus() : crossResponseMsgDTO.getCrossStatusCode() );
        try {
            dbCommonRPCComponent.apiMerchantCardService.updateById(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
    }

    @Override
    public RegisterCollectTable getRegisterInfoTableByPlatformOrderId(String platformOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRegisterInfoTableByPlatformOrderId";
        RegisterCollectTable registerCollectTable = null;
        try {
            registerCollectTable = dbCommonRPCComponent.apiRegisterCollectService.getOne(new RegisterCollectTable()
                    .setPlatformOrderId(platformOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setStatus(StatusEnum._0.getStatus())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据平台流水号获取进件成功的附属表,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(registerCollectTable,
                ResponseCodeEnum.RXH00032.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;"
                        ,ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00032.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00032.getMsg()));

        return registerCollectTable;
    }


    @Override
    public void checkSuccessBondCardInfo(MerchantCardTable merchantCardTable,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkSuccessBondCardInfo";
        MerchantCardTable merchantCardTable1 = null;
        try{
            merchantCardTable1 = dbCommonRPCComponent.apiMerchantCardService.getOne(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取进件成功的所有通道时，发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNotNull(merchantCardTable1,
                ResponseCodeEnum.RXH00028.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00028.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00028.getMsg()));
    }


    @Override
    public void updateByBondCardInfoByB6(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, MerchantCardTable merchantCardTable_old, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByBondCardInfoByB6";
        merchantCardTable.setCrossRespResult(crossResponseMsg)
                .setChannelRespResult( null == crossResponseMsgDTO ? null  : crossResponseMsgDTO.getChannelResponseMsg() )
                .setStatus( null == crossResponseMsgDTO ? StatusEnum._1.getStatus() : crossResponseMsgDTO.getCrossStatusCode() );

        merchantCardTable_old.setStatus(StatusEnum._5.getStatus());

        List<MerchantCardTable> list = new ArrayList<>(2);
        list.add(merchantCardTable);
        list.add(merchantCardTable_old);
        try {
            dbCommonRPCComponent.apiMerchantCardService.bachUpdateById(list);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

    }




    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple4<RegisterInfoTable,RegisterCollectTable, ChannelExtraInfoTable,MerchantCardTable> tuple4 = (Tuple4<RegisterInfoTable, RegisterCollectTable, ChannelExtraInfoTable, MerchantCardTable>) tuple;
        return new RequestCrossMsgDTO()
                .setMerchantCardTable(tuple4._4)
                .setChannelExtraInfoTable(tuple4._3)
                .setRegisterInfoTable(tuple4._1)
                .setRegisterCollectTable(tuple4._2);
    }

    @Override
    public Map<String, ParamRule> getParamMapByB4() {
        return new HashMap<String, ParamRule>() {
            {//
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));// 商户订单号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行简码	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankName", new ParamRule(ParamTypeEnum.STRING.getType(), 4, 32));//银行名称
                put("bankAccountProp", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//账户属性
                put("subBankName", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//支行名称
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB5() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB6() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//返回地址
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//通知地址
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
                put("smsCode", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 6));//短信验证码
                put("payFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 3, 6));//代付手续费
                put("backFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 3, 6));//代付手续费
                put("backCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//还款银行卡号
                put("backBankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//还款银行编码
                put("backCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//还款银行卡手机号
            }
        };
    }


}
