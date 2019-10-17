package com.internal.playment.api.tools;


import com.internal.playment.api.exception.PayException;

/**
 *   断言接口
 * @author panda
 * @date 20190718
 */
public interface PayAssert {

    /**
     * 空对象抛出异常
     * @param object
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws PayException
     */
    default void isNull(Object object, String code, String innerPrintMsg, String responseMsg) throws PayException {
        if(null == object)
            throw new PayException(code,innerPrintMsg,responseMsg);
    }
}
