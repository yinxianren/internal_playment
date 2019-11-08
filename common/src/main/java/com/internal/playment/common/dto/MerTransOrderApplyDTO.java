package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/5
 * Time: 下午7:24
 * Description:
 */
@Data
public class MerTransOrderApplyDTO implements Serializable {
    private String charset;//	字符集编码	固定UTF-8
    private String signType;//	签名类型	固定为MD5
    private String productType;//	产品类型
    private String merId;//	商户号	我司分配给接入方的唯一编码
    private String terMerId;//	子商户id
    private String terMerName;//	子商户名称	商户系统中商户的名称
    private String merOrderId;//	商户订单号	要求唯一
    private String orgMerOrderId;//	原交易商户订单号	可传多笔,最多10笔，用”| ”隔开
    private String currency;//	币种	交易的币种，固定传CNY
    private String amount;//	代付金额	代付金额≤原订单总金额-代付手续费
    private String identityType;//	证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证
    private String identityNum;//	证件号码
    private String cardHolderName;
    private String bankCode;//	银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String bankCardType;//	卡号类型	1借记卡  2信用卡
    private String bankCardNum;//	银行卡号
    private String bankCardPhone;//	银行卡手机号
    private String backFee;//	代付手续费	单笔固定金额
    private String returnUrl;//	返回地址
    private String noticeUrl;//	通知地址
    private String signMsg;//	签名字符串
}
