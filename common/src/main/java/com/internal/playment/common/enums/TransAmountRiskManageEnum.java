package com.internal.playment.common.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum TransAmountRiskManageEnum {
    T_SINGLE_MIN_1(new BigDecimal(1),"单笔代付金额至少 1 RMB"),
    T_MIN_10(new BigDecimal(10),"代付金额至少 10 RMB"),
    T_MAX_10W(new BigDecimal(100000),"代付金额每次交易不超过 10W RMB");

    private BigDecimal amount;
    private String remark;

    TransAmountRiskManageEnum(BigDecimal amount,String remark){
        this.amount = amount ;
        this.remark = remark;
    }
}
