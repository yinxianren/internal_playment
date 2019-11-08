package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午4:26
 * Description:
 */
@Data
public class MerConfirmBondCardDTO implements Serializable {
    private String charset;//	字符集编码	固定UTF-8
    private String signType;//	签名类型	固定为MD5
    private String merId;//	商户号	我司分配给接入方的唯一编码
    private String platformOrderId;//	平台流水号	绑卡申请接口成功或者绑卡短信获取时，返回的平台流水号
    private String cardHolderName;//	姓名	银行卡持有者的姓名
    private String identityType;//	证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String identityNum;//	证件号码
    private String bankCode;//	银行名称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String bankCardType;//	卡号类型	1借记卡  2信用卡
    private String bankCardNum;//	银行卡号
    private String bankCardPhone;//	银行卡手机号
    private String validDate;//	有效期	信用卡必填，格式：MMYY
    private String securityCode;//	安全码	信用卡必填，信用卡背面三位安全码
    private String payFee;//	扣款手续费
    private String backFee;//	代付手续费
    private String backCardNum;//	还款银行卡号
    private String backBankCode;//	还款银行编码
    private String backCardPhone;//	还款银行卡手机号
    private String smsCode;//	短信验证码
    private String terMerId;//	子商户id
    private String terMerName;//	子商户名称	商户系统中商户的名称
    private String returnUrl;//	返回地址
    private String noticeUrl;//	通知地址
    private String signMsg;//	签名字符串
}
