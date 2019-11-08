package com.internal.playment.common.inner;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 下午1:51
 * Description:
 */
@Data
public class ParamRule {

    private int type;
    private int maxLength;
    private int minLength;

    public ParamRule(int type, int minLength, int maxLength) {
        this.type = type;
        this.maxLength = maxLength;
        this.minLength = minLength;
    }
}
