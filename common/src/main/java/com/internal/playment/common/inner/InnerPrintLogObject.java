package com.internal.playment.common.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 下午3:06
 * Description:
 */

@Getter
@AllArgsConstructor
public class InnerPrintLogObject {
    
    private String merId;
    private String terMerId;
    private String bussType;



    public InnerPrintLogObject setMerId(String merId) {
        this.merId = merId;
        return this;
    }

    public InnerPrintLogObject setTerMerId(String terMerId) {
        this.terMerId = terMerId;
        return this;
    }

    public InnerPrintLogObject setBussType(String bussType) {
        this.bussType = bussType;
        return this;
    }
}
