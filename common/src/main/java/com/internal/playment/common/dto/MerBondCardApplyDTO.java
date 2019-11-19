package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午10:13
 * Description:
 */
@Data
public class MerBondCardApplyDTO implements Serializable {

    private String charset;//	参数字符集编码	固定UTF-8
    private String signType;//	签名类型	固定为MD5
    private String merId;//	商户号	我司分配给接入方的唯一编码
    private String merOrderId;//	商户订单号	要求唯一
    private String platformOrderId;//
    private String cardHolderName;//持卡人姓名
    private String identityType;//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String identityNum;//证件号码
    private String bankCode;//银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String bankName;//银行名称
    private String bankAccountProp;//账户属性	0：个人账户，1：对公账户
    private String subBankName;//支行名称
    private String bankCardType;//卡号类型	1借记卡  2信用卡
    private String bankCardNum;//银行卡号
    private String bankCardPhone;//银行卡手机号
    private String validDate;//有效期	信用卡必填，格式：MMYY
    private String securityCode;//安全码	信用卡必填，信用卡背面三位安全码
    private String terMerId;//子商户id	商户系统中商户的编码，要求唯一
    private String terMerName;//子商户名称	商户系统中商户的名称
    private String returnUrl;//返回地址
    private String noticeUrl;//通知地址
    private String signMsg;//签名字符串
}
