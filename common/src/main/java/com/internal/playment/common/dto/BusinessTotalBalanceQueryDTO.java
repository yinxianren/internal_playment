package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessTotalBalanceQueryDTO implements Serializable {
    private String charset;//	字符集编码
    private String signType;//	签名类型
    private String merId;//	商户号
    private String terMerId;//	子商户id
    private String signMsg;//	签名字符串
}
