package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessCusInfoQueryDTO implements Serializable {
    private String charset;//	字符集编码	固定UTF-8
    private String signType;//	签名类型	固定为MD5
    private String merId;//	商户号	我司分配给接入方的唯一编码
    private String terMerId;//	子商户id	商户系统中商户的编码，要求唯一
    private String signMsg;//	签名字符串
}
