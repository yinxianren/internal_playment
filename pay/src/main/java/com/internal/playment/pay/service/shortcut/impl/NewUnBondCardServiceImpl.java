package com.internal.playment.pay.service.shortcut.impl;

import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple3;
import com.internal.playment.pay.service.CommonServiceAbstract;
import com.internal.playment.pay.service.shortcut.NewUnBondCardService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NewUnBondCardServiceImpl extends CommonServiceAbstract implements NewUnBondCardService {

    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) throws NewPayException {
        Tuple3<MerchantCardTable,RegisterCollectTable,ChannelExtraInfoTable> tuple3 = (Tuple3<MerchantCardTable,RegisterCollectTable,ChannelExtraInfoTable>) tuple;
        return new RequestCrossMsgDTO()
                .setMerchantCardTable(tuple3._1)
                .setRegisterCollectTable(tuple3._2)
                .setChannelExtraInfoTable(tuple3._3);
    }

    @Override
    public Map<String, ParamRule> getParamMapByUnBondCard() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("channelTab", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//通道标识
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//银行卡号
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//证件号码
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public OrganizationInfoTable getOrganizationInfoByUnBondCard(String organizationId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="OrganizationInfoTable";
        OrganizationInfoTable organizationInfoTable = null;
        try{
            organizationInfoTable = dbCommonRPCComponent.apiOrganizationInfoService.getOne(
                    new OrganizationInfoTable()
                            .setOrganizationId(organizationId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取组织机构信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(organizationInfoTable,
                ResponseCodeEnum.RXH00061.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：根据机构ID(%s),获取组织信息为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00061.getMsg(),localPoint,organizationId),
                format(" %s",ResponseCodeEnum.RXH00061.getMsg()));

        state(organizationInfoTable.getStatus() == StatusEnum._1.getStatus(),
                ResponseCodeEnum.RXH00062.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：根据机构ID(%s),获取组织信息为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00062.getMsg(),localPoint,organizationId),
                format(" %s",ResponseCodeEnum.RXH00062.getMsg()));

        isNull(organizationInfoTable.getApplicationClassObj(),
                ResponseCodeEnum.RXH99996.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：组织机构中有字段未配置：ApplicationClassObj==null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH99996.getMsg(),localPoint,organizationId),
                format(" %s",ResponseCodeEnum.RXH99996.getMsg()));

        return organizationInfoTable;
    }

    @Override
    public void update(MerchantCardTable setUpdateTime, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="update(MerchantCardTable setUpdateTime, InnerPrintLogObject ipo)";
        try{
            dbCommonRPCComponent.apiMerchantCardService.updateById(setUpdateTime);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新解卡信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
    }
}
