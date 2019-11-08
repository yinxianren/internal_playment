package com.internal.playment.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午8:51
 * Description:
 */
@Data
public class MerServiceFulfillDTO implements Serializable {
    private String charset;//	字符集编码
    private String signType;//	签名类型
    private String merId;//	商户号
    private String terMerId;//	子商户id
    private String platformOrderId;//	平台流水号
    private String signMsg;//	签名字符串
}
