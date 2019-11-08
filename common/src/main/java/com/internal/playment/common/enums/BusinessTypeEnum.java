package com.internal.playment.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午3:26
 * Description:
 */
@Getter
public enum BusinessTypeEnum {

    PAY("pay","收单"),
    TRANS("trans","代付"),

    C("C","通道"),
    M("M","商户"),

    b1("b1","业务登记"),
    b2("b2","绑定银行卡"),
    b3("b3","业务开通"),
    b4("b4","绑卡申请"),
    b5("b5","绑卡验证码"),
    b6("b6","确认绑卡"),
    b7("b7","支付申请"),
    b8("b8","支付验证码"),
    b9("b9","确认交易"),
    b10("b10","无验证支付"),
    b11("b11","付款")
    ;
    private String busiType;
    private String remark;
    BusinessTypeEnum(String busiType,String remark){
        this.busiType = busiType;
        this.remark = remark;
    }
}
