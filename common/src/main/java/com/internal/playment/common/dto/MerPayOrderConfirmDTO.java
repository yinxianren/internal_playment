package com.internal.playment.common.dto;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午2:32
 * Description:
 */
@Data
public class MerPayOrderConfirmDTO {
    private String charset;//	字符集编码	固定UTF-8	否	5
    private String signType	;//签名类型	固定为MD5	否	3
    private String merId;//	商户号	我司分配给接入方的唯一编码	否	32
    private String terMerId;//	子商户id		否	64
    private String platformOrderId;//	平台订单号	支付申请返回的平台订单号	是	64
    private String smsCode;//	短信验证码		否	16
    private String signMsg;//	签名字符串		否	256
}
