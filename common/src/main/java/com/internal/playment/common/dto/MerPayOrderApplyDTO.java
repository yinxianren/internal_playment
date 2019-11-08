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
public class MerPayOrderApplyDTO implements Serializable {
    private String  charset;//	字符集编码	固定UTF-8
    private String  signType;//	签名类型	固定为MD5
    private String  productType	;//产品类型
    private String  merId;//	商户号	我司分配给接入方的唯一编码
    private String  productCategory;//	商品类别	参考附件：商户入住字典表
    private String  merOrderId;//	商户订单号	每次发起请求，要求唯一
    private String  platformOrderId;//	平台订单号	确认绑卡成功时，返回的唯一值
    private String  currency;//	币种	交易的币种，固定传CNY
    private String  amount;//	支付金额
    private String  identityType;//	证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String  identityNum;//	证件号码
    private String   bankCode;//	银行名称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String  bankCardType;//	卡号类型	1借记卡  2信用卡
    private String  bankCardNum;//	银行卡号
    private String  bankCardPhone;//	银行卡手机号
    private String  validDate;//	有效期	信用卡必填，格式：MMYY
    private String  securityCode;//	安全码	信用卡必填，信用卡背面三位安全码
    private String  payFee;//	扣款手续费
    private String  terMerId;//	子商户id
    private String  terMerName;//	子商户名称	商户系统中商户的名称
    private String  province;//	省份	以地区对照表省市码为准，参照3.2地区对照表
    private String   city;//	城市	以地区对照表省市码为准，参照3.2地区对照表
    private String   returnUrl;//	返回地址
    private String   noticeUrl;//	通知地址
    private String   deviceId;//	交易设备号	交易设备号 (包含但不限于POS终端号或者手机IMEI) 字符串类型，最大长度50位,IMEI：国际移动设备识别码（International Mobile Equipment Identity，IMEI），即通常所说的手机序列号、手机“串号”，用于在移动电话网络中识别每一部独立的手机等移动通信设备，相当于移动电话的身份证。
    private String  deviceType;//	付款用户设备类型	交易设备类型(1-电脑;2-手机;3-其他) 字符串类型
    private String   macAddress;//	MAC地址	MAC地址 (直译为媒体访问控制地址，也称为局域网地址（LAN Address），以太网地址（Ethernet Address）或物理地址（Physical Address），它是一个用来确认网上设备位置的地址。)字符串类型 最大长度30
    private String   signMsg;//	签名字符串
}
