package com.internal.playment.common.enums;

public enum ProductGroupTypeEnum {

    RH_SMALL_PRODUCT("RH_SMALL_PRODUCT","小额产品组"),//通联
    RH_LARGE_PRODUCT("RH_LARGE_PRODUCT","大额产品组");

    private String groupId;
    private String productName;

    ProductGroupTypeEnum(String groupId, String productName){
        this.groupId = groupId;
        this.productName = productName;
    }
}
