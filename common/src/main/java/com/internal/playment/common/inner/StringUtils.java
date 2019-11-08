package com.internal.playment.common.inner;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/24
 * Time: 10:46
 * Project: Management
 * Package: com.rxh.utils
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * StringUtils.getStrInsteadOfNull(null)        == ""
     * StringUtils.getStrInsteadOfNull("")          == ""
     * StringUtils.getStrInsteadOfNull("abc")       == "abc"
     *
     * @param str 传入字符串
     * @return 不为Null的字符串
     */
    public static String getToStringInsteadOfNull(final Object str) {
        return str == null ? EMPTY : str.toString();
    }

    /**
     * VieWay加密顺序处理
     *
     * @param str    加密顺序
     * @param regex  加密顺序分隔符
     * @param values 字段值
     * @return
     */
    public final static String vieWaySplit(String str, String regex, Map<String, String> values) {
        if (isNotBlank(str) && isNoneBlank(regex) && isNoneBlank(values.toString())) {
            if (equals("|", regex)) {
                regex = "\\|";
            }
            String chr = "";
            StringBuffer result = new StringBuffer();
            for (String s :
                    str.split(regex)) {
                result.append(s);
                result.append("=");
                chr = (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
                if (values.containsKey(chr)) {
                    result.append(values.get(chr));
                }
                result.append("&");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }
        return "";
    }

    /**
     * 判断是否超过长度
     *
     * @param str
     * @param length
     * @return
     */
    public final static String limitLength(String str, int length) {
        if (isNotBlank(str)) {
            str = str.length() > length ? str.substring(0, length - 1) : str;
        }
        return str;
    }
}
