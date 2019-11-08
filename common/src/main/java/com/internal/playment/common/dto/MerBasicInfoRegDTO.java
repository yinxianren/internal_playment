package com.internal.playment.common.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class MerBasicInfoRegDTO implements Serializable {
    private String  charset;//	字符编码	固定UTF-8
    private String  signType;//	签名类型	固定为MD5
    private String  productType;//	产品类型
    private String  merId;//	商户号	我司分配给接入方的唯一编码
    private String  merOrderId;//	商户订单号	每次发起请求，要求唯一
    private String  merType;//	商户类型	00公司商户，01个体商户
    private String  terMerId;//	子商户id	商户系统中商户的编码，要求唯一
    private String  terMerName;//	子商户名称	商户系统中商户的名称
    private String  terMerShortName;//	商户简称
    private String  category;//	经营类目	参考3.3
    private String  identityType;//	证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String  identityNum;//	证件号码
    private byte[]  miMerCertPic1;//	资质影印件	身份证正面照图片转为BASE64编码，图片大小在512K以内
    private byte[]  miMerCertPic2;//	资质影印件	身份证反面照图片转为BASE64编码，图片大小在512K以内
    private String  phone;//	手机号
    private String  province;//	省份	以地区对照表省市码为准，参照3.2地区对照表
    private String  city;//	城市	以地区对照表省市码为准，参照3.2地区对照表
    private String  address;//	详细地址
    private String  bankCode;//	银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String  bankCardType;//	卡号类型	1借记卡  2信用卡
    private String  cardHolderName;//	持卡人姓名
    private String  bankCardNum;//	银行卡号
    private String  bankCardPhone;//	银行卡手机号
    private String  payFee;//	扣款手续费	用户扣款费率，单位： %，如2.8
    private String  backFee;//	代付手续费	用户还款费率,单位：元/笔,保留两位小数
    private String  signMsg;//	签名字符串


}
