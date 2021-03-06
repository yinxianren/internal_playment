package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessOrderQueryDTO implements Serializable {
    private String charset;//	字符集编码
    private String signType;//	签名类型
    private String productType;//	产品类型
    private String merId;//	商户号
    private String terMerId;//	子商户id
    private String merOrderId;//	商户订单号
    private String signMsg;//	签名字符串
}
