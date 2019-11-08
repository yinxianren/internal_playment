package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午6:54
 * Description:
 */
@Data
public class MerNoAuthPayOrderApplyDTO implements Serializable {
    private String  charset;//	字符集编码	固定UTF-8
    private String  signType;//	签名类型	固定为MD5
    private String  productType;//	产品类型
    private String  merId;//	商户号	我司分配给接入方的唯一编码
    private String  terMerId;//	子商户id
    private String  terMerName;//	子商户名称	商户系统中商户的名称
    private String  productCategory;//	商品类别	参考附件：商户入住字典表
    private String  merOrderId;//	商户订单号	要求唯一
    private String  currency;//	币种	交易的币种，固定传CNY
    private String  amount;//	支付金额
    private String  identityType;//	证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String  cardHolderName;//	姓名	卡持有者的姓名
    private String  identityNum;//	证件号码
    private String  bankCode;//	银行名称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String  bankCardType;//	卡号类型	1借记卡  2信用卡
    private String  bankCardNum;//	银行卡号
    private String  bankCardPhone;//	银行卡手机号
    private String  validDate;//	有效期	信用卡必填，格式：MMYY
    private String  securityCode;//	安全码	信用卡必填，信用卡背面三位安全码
    private String  payFee;//	扣款手续费
    private String  province;//	省份	以地区对照表省市码为准，参照3.2地区对照表
    private String  city;//	城市	以地区对照表省市码为准，参照3.2地区对照表
    private String  returnUrl;//	返回地址
    private String  noticeUrl;//	通知地址
    private String  signMsg	;//签名字符串
}
