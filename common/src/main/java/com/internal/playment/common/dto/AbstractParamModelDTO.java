package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  公共参数字段
 * @date 20190628
 */
@Data
public abstract class AbstractParamModelDTO implements Serializable {

    protected String returnUrl;// 返回地址
    protected String noticeUrl;  // 通知地址
    protected String md5Info; // 签名字符串
    private   String  signMsg; //  签名字符串
    protected String merId; // 商户号
    protected String terminalMerId; //用用终端号
    protected String merOrderId; // 商户订单号
    protected String amount; // 代付金额

}
