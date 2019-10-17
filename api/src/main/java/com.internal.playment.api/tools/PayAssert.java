package com.internal.playment.api.tools;


import com.internal.playment.api.exception.PayException;

/**
 *   断言接口
 * @author panda
 * @date 20190718
 */
public interface PayAssert {

    /**
     *
     * @param bl
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws PayException
     */
    default void isTrue(boolean bl,String code, String innerPrintMsg, String responseMsg) throws PayException {
         if(bl){
             throw new PayException(code,innerPrintMsg,responseMsg);
         }
     }
    /**
     *
     * @param cs
     * @return
     */
    default void isBlank(final CharSequence cs,String code, String innerPrintMsg, String responseMsg) throws PayException {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            throw new PayException(code,innerPrintMsg,responseMsg);
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return ;
            }
        }
        throw new PayException(code,innerPrintMsg,responseMsg);
    }
    default boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
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
