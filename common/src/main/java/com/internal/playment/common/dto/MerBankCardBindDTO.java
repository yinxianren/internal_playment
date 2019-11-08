package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午5:13
 * Description:
 */
@Data
public class MerBankCardBindDTO implements Serializable {
    private String charset	;//参数字符集编码
    private String signType;//	签名类型
    private String merId;//	商户号
    private String terMerId;//	子商户id
    private String platformOrderId;//平台流水号
    private String bankAccountProp;//	账户属性
    private String identityType;//	证件类型
    private String identityNum;//	证件号码
    private String bankCode;//	银行简称
    private String bankCardType;//	卡号类型
    private String cardHolderName;//	持卡人姓名
    private String bankCardNum;//	银行卡号
    private String bankCardPhone;//	银行卡手机号
    private String province;//	省份
    private String city;//	城市
    private String signMsg;//	签名字符串
}
