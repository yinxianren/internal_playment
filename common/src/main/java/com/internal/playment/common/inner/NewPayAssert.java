package com.internal.playment.common.inner;


import java.util.Collection;
import java.util.Map;

public interface NewPayAssert {

    /**
     *
     * @param bl
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws NewPayException
     */
    default void isTrue(boolean bl, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(bl){
            throw new NewPayException(code,innerPrintMsg,responseMsg);
        }
    }
    /**
     *
     * @param cs
     * @return
     */
    default void isBlank(final CharSequence cs, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            throw new NewPayException(code,innerPrintMsg,responseMsg);
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return ;
            }
        }
        throw new NewPayException(code,innerPrintMsg,responseMsg);
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
     * @throws NewPayException
     */
    default void isNull(Object object, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null == object)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }

    default boolean isNull(Object object){
        if(null == object) return true;
        return false;
    }


    /**
     * 空对象抛出异常
     * @param object
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws NewPayException
     */
    default void isNotNull(Object object, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null != object)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }


    /**
     *
     * @param collection
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws NewPayException
     */
    default void isHasNotElement(Collection collection, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null == collection || collection.size() == 0)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }

    default void isHasNotElement(Map map, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null == map || map.size() == 0)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }

    default boolean isHasNotElement(Collection collection){
        if(null == collection || collection.size() == 0) return true;
        return false;
    }
    default boolean isHasElement(Collection collection){
        if(null == collection || collection.size() == 0) return false;
        return true;
    }

    default void isHasElement(Collection collection, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null == collection || collection.size() != 0)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }

    default void state(boolean state, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(state)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }
}
