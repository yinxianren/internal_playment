package com.internal.playment.common.enums;

import lombok.Getter;

@Getter
public enum ProductTypeEnum {

    RH_QUICKPAY_SMALL("RH_QUICKPAY_SMALL","快捷小额"),
    RH_QUICKPAY_SMALL_NOSMS("RH_QUICKPAY_SMALL_NOSMS", "无验证快捷小额"),
    RH_QUICKPAY_LARGE ("RH_QUICKPAY_LARGE","快捷大额"),
    RH_QUICKPAY_SMALL_REP("RH_QUICKPAY_SMALL_REP","快捷小额信还"),
    RH_QUICKPAY_SMALL_NOSMS_REP("RH_QUICKPAY_SMALL_NOSMS_REP","无验证快捷小额信还"),
    RH_QUICKPAY_LARGE_REP("RH_QUICKPAY_LARGE_REP","快捷大额信还"),
    RH_TRX_PAY("RH_TRX_PAY","代付");

    private String productId;
    private String productName;

    ProductTypeEnum(String productId, String productName){
        this.productId = productId;
        this.productName = productName;
    }
}
